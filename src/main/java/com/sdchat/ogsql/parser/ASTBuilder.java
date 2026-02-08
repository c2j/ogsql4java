package com.sdchat.ogsql.parser;

import com.sdchat.ogsql.grammar.*;
import com.sdchat.ogsql.ast.*;
import com.sdchat.ogsql.exception.SemanticErrorException;
import com.sdchat.ogsql.exception.SyntaxErrorException;
import com.sdchat.ogsql.exception.InputValidationException;
import org.antlr.v4.runtime.*;
import java.util.List;
import java.util.ArrayList;

public class ASTBuilder extends OpenGaussSQLBaseVisitor<SQLStatement> {

    @Override
    public SQLStatement visitRoot(OpenGaussSQLParser.RootContext ctx) {
        if (ctx.stmtblock() != null && ctx.stmtblock().stmtmulti() != null) {
            OpenGaussSQLParser.StmtmultiContext stmtmulti = ctx.stmtblock().stmtmulti();
            List<OpenGaussSQLParser.StmtContext> stmtList = stmtmulti.stmt();
            
            for (OpenGaussSQLParser.StmtContext stmtCtx : stmtList) {
                if (stmtCtx != null) {
                    SQLStatement stmt = stmtCtx.accept(this);
                    if (stmt != null) {
        return stmt;
    }
                }
            }
        }
        return null;
    }

    @Override
    public SQLStatement visitStmt(OpenGaussSQLParser.StmtContext ctx) {
        if (ctx.selectstmt() != null) {
            return visitSelectstmt(ctx.selectstmt());
        } else if (ctx.insertstmt() != null) {
            return visitInsertstmt(ctx.insertstmt());
        } else if (ctx.updatestmt() != null) {
            return visitUpdatestmt(ctx.updatestmt());
        } else if (ctx.deletestmt() != null) {
            return visitDeletestmt(ctx.deletestmt());
        } else if (ctx.createstmt() != null) {
            return visitCreatestmt(ctx.createstmt());
        } else if (ctx.createschemastmt() != null) {
            return visitCreateschemastmt(ctx.createschemastmt());
        } else if (ctx.createprocedurestmt() != null) {
            return visitCreateprocedurestmt(ctx.createprocedurestmt());
        } else if (ctx.alterprocedurestmt() != null) {
            return visitAlterprocedurestmt(ctx.alterprocedurestmt());
        } else if (ctx.alterstmt() != null) {
            return visitAlterstmt(ctx.alterstmt());
        } else if (ctx.dropstmt() != null) {
            return visitDropstmt(ctx.dropstmt());
        } else if (ctx.callstmt() != null) {
            return visitCallstmt(ctx.callstmt());
        } else if (ctx.setstmt() != null) {
            return visitSetstmt(ctx.setstmt());
        }
        return null;
    }

    @Override
    public SQLStatement visitSelectstmt(OpenGaussSQLParser.SelectstmtContext ctx) {
        SelectQuery query = new SelectQuery();

        if (ctx.hintComment() != null && ctx.hintComment().HINT_COMMENT() != null) {
            List<PerformanceHint> hints = parseHintContent(ctx.hintComment().HINT_COMMENT().getText());
            for (PerformanceHint hint : hints) {
                query.addHint(hint);
            }
        }

        if (ctx.fromclause() != null && ctx.fromclause().fromExpression() != null) {
            StringBuilder fromClause = new StringBuilder();
            OpenGaussSQLParser.FromExpressionContext fromExpr = ctx.fromclause().fromExpression();

            // Handle table expressions (handles both comma-separated and first table in JOIN)
            if (fromExpr.tableExpression() != null) {
                boolean first = true;
                for (OpenGaussSQLParser.TableExpressionContext tableExpr : fromExpr.tableExpression()) {
                    if (!first) {
                        fromClause.append(",");
                    }
                    first = false;

                    if (tableExpr.tableReference() != null) {
                        // Handle both simple table name and subquery
                        if (tableExpr.tableReference().IDENTIFIER() != null) {
                            fromClause.append(tableExpr.tableReference().IDENTIFIER().getText());

                            // Create DataSource for structured access (P2 optimization)
                            DataSource dataSource = new DataSource(tableExpr.tableReference().IDENTIFIER().getText());
                            if (tableExpr.optalias() != null) {
                                String alias = tableExpr.optalias().getText();
                                if (alias != null && !alias.isEmpty()) {
                                    fromClause.append(" ").append(alias);
                                    dataSource.setAlias(alias);
                                }
                            }
                            query.addDataSource(dataSource);

                        } else if (tableExpr.tableReference().selectstmt() != null) {
                            // For subquery, only append alias, not subquery content
                            if (tableExpr.optalias() != null) {
                                String aliasText = tableExpr.optalias().getText();
                                if (aliasText != null && !aliasText.isEmpty()) {
                                    // Strip "AS" prefix using simple String operations
                                    String finalAlias = aliasText.trim();
                                    if (finalAlias.toUpperCase().startsWith("AS")) {
                                        finalAlias = finalAlias.substring(2).trim();
                                    }
                                    fromClause.append(" ").append(finalAlias);
                                }
                            }

                            // Note: Subquery DataSource creation would require recursively visiting of subquery
                            // For now, we only create DataSource for simple table references
                        }
                    }
                }
            }
 
            // Handle join expressions
            if (fromExpr.joinExpression() != null) {
                for (OpenGaussSQLParser.JoinExpressionContext joinExpr : fromExpr.joinExpression()) {
                    if (joinExpr.joinType() != null) {
                        fromClause.append(" ").append(joinExpr.joinType().getText());
                    }
                    if (joinExpr.tableExpression() != null) {
                        if (joinExpr.tableExpression().tableReference() != null) {
                            String tableRef = joinExpr.tableExpression().tableReference().getText();
                            if (tableRef != null && !tableRef.isEmpty()) {
                                fromClause.append(" ").append(tableRef);

                                // Create DataSource for joined tables (P2 optimization)
                                DataSource dataSource = new DataSource(tableRef);
                                if (joinExpr.joinType() != null) {
                                    dataSource.setJoinType(joinExpr.joinType().getText());
                                }
                                if (joinExpr.tableExpression().optalias() != null) {
                                    String alias = joinExpr.tableExpression().optalias().getText();
                                    if (alias != null && !alias.isEmpty()) {
                                        fromClause.append(" ").append(alias);
                                        dataSource.setAlias(alias);
                                    }
                                }
                                query.addDataSource(dataSource);
                            }
                        }
                    }
                }
            }
            
            query.setFromClause(fromClause.toString());
        }

        return query;
    }

    private List<PerformanceHint> extractHints(OpenGaussSQLParser.HintBlockContext hintBlockCtx) {
        List<PerformanceHint> hints = new ArrayList<>();
        
        if (hintBlockCtx == null || hintBlockCtx.HINT_COMMENT() == null) {
            return hints;
        }
        
        // Get the hint comment text
        String hintComment = hintBlockCtx.HINT_COMMENT().getText();
        
        // Parse the hint content
        return parseHintContent(hintComment);
    }

    private PerformanceHint extractHint(OpenGaussSQLParser.HintItemContext hintItemCtx) {
        if (hintItemCtx.NESTLOOP() != null) {
            List<String> tables = hintItemCtx.tableHintList() != null ? extractTables(hintItemCtx.tableHintList()) : new ArrayList<>();
            return new PerformanceHint("NestLoop", tables);
        } else if (hintItemCtx.MERGEJOIN() != null) {
            List<String> tables = hintItemCtx.tableHintList() != null ? extractTables(hintItemCtx.tableHintList()) : new ArrayList<>();
            return new PerformanceHint("MergeJoin", tables);
        } else if (hintItemCtx.HASHJOIN() != null) {
            List<String> tables = hintItemCtx.tableHintList() != null ? extractTables(hintItemCtx.tableHintList()) : new ArrayList<>();
            return new PerformanceHint("HashJoin", tables);
        }
        return null;
    }

    private List<String> extractTables(OpenGaussSQLParser.TableHintListContext tableHintListCtx) {
        List<String> tables = new ArrayList<>();
        if (tableHintListCtx != null) {
            for (int i = 0; i < tableHintListCtx.IDENTIFIER().size(); i++) {
                tables.add(tableHintListCtx.IDENTIFIER(i).getText());
            }
        }
        return tables;
    }

    /**
     * Parses hint content to extract individual performance hints.
     * 
     * @param hintContent The full hint content including hint delimiters
     * @return List of extracted performance hints
     * @throws com.sdchat.ogsql.exception.ParseException if hint syntax is invalid
     */
    private List<PerformanceHint> parseHintContent(String hintContent) {
        List<PerformanceHint> hints = new ArrayList<>();
        
        // Remove the hint delimiters
        String content = hintContent.replace("/*+", "").replace("*/", "").trim();
        
        // Parse individual hint items - look for patterns like NestLoop(table1, table2)
        if (content.isEmpty()) {
            return hints; // Empty hint
        }
        
        // Use regex to find all hint patterns: HintName(table1, table2, ...)
        java.util.regex.Pattern hintPattern = java.util.regex.Pattern.compile("(\\w+)\\s*\\(([^)]*)\\)");
        java.util.regex.Matcher matcher = hintPattern.matcher(content);
        
        while (matcher.find()) {
            String hintType = matcher.group(1).trim();
            String tablesPart = matcher.group(2).trim();
            
            // Validate hint type
            if (!isValidHintType(hintType)) {
                throw new com.sdchat.ogsql.exception.ParseException("Invalid hint type: " + hintType);
            }
            
            List<String> tables = new ArrayList<>();
            if (!tablesPart.isEmpty()) {
                String[] tableNames = tablesPart.split(",");
                for (String tableName : tableNames) {
                    tableName = tableName.trim();
                    if (!tableName.isEmpty()) {
                        tables.add(tableName);
                    }
                }
            }
            
            // Validate that tables are specified for join hints
            if (tables.isEmpty() && (hintType.equals("NestLoop") || hintType.equals("MergeJoin") || hintType.equals("HashJoin"))) {
                throw new com.sdchat.ogsql.exception.ParseException("Hint " + hintType + " requires table list");
            }
            
            hints.add(new PerformanceHint(hintType, tables));
        }
        
        // If no valid hints were found, throw an exception
        if (hints.isEmpty() && !content.isEmpty()) {
            throw new com.sdchat.ogsql.exception.ParseException("Invalid hint format: " + content);
        }
        
        return hints;
    }
    
    /**
     * Validates if a hint type is supported.
     * 
     * @param hintType The hint type to validate
     * @return true if the hint type is valid, false otherwise
     */
    private boolean isValidHintType(String hintType) {
        return hintType.equals("NestLoop") || hintType.equals("MergeJoin") || hintType.equals("HashJoin");
    }

    @Override
    public SQLStatement visitInsertstmt(OpenGaussSQLParser.InsertstmtContext ctx) {
        InsertStatement stmt = new InsertStatement();
        return stmt;
    }

    @Override
    public SQLStatement visitUpdatestmt(OpenGaussSQLParser.UpdatestmtContext ctx) {
        UpdateStatement stmt = new UpdateStatement();
        return stmt;
    }

    @Override
    public SQLStatement visitDeletestmt(OpenGaussSQLParser.DeletestmtContext ctx) {
        DeleteStatement stmt = new DeleteStatement();
        return stmt;
    }

    @Override
    public SQLStatement visitCreatestmt(OpenGaussSQLParser.CreatestmtContext ctx) {
        // Check if this is a CREATE FOREIGN TABLE statement
        if (ctx.createForeignTableStmt() != null) {
            return visitCreateForeignTableStmt(ctx.createForeignTableStmt());
        }
        
        // Regular CREATE TABLE statement
        CreateStatement stmt = new CreateStatement();
        
        // Extract table name
        if (ctx.IDENTIFIER() != null) {
            stmt.setObjectName(ctx.IDENTIFIER().getText());
        }
        
        // Extract table elements (columns and constraints)
        if (ctx.opttableelementlist() != null && ctx.opttableelementlist().tableelementlist() != null) {
            OpenGaussSQLParser.TableelementlistContext tableElementList = ctx.opttableelementlist().tableelementlist();
            for (OpenGaussSQLParser.TableelementContext tableElement : tableElementList.tableelement()) {
                if (tableElement.columnDef() != null) {
                    Column column = extractColumnDef(tableElement.columnDef());
                    if (column != null) {
                        stmt.addColumn(column);
                    }
                } else if (tableElement.tableconstraint() != null) {
                    Constraint constraint = extractTableconstraint(tableElement.tableconstraint());
                    if (constraint != null) {
                        stmt.addConstraint(constraint);
                    }
                }
            }
        }
        
        // Extract partitioning information
        if (ctx.optPartitionBy() != null) {
            PartitioningInformation partitioning = extractOptPartitionBy(ctx.optPartitionBy());
            if (partitioning != null) {
                stmt.setPartitioning(partitioning);
            }
        }
        
        return stmt;
    }

    @Override
    public SQLStatement visitDropstmt(OpenGaussSQLParser.DropstmtContext ctx) {
        if (ctx == null || ctx.IDENTIFIER() == null) {
            return null;
        }

        DropStatement stmt = new DropStatement();
        stmt.setObjectName(ctx.IDENTIFIER().getText());

        if (ctx.IF() != null && ctx.EXISTS() != null) {
            stmt.setIfExists(true);
        }

        if (ctx.CASCADE() != null) {
            stmt.setCascade(true);
        }

        return stmt;
    }

    @Override
    public SQLStatement visitAlterstmt(OpenGaussSQLParser.AlterstmtContext ctx) {
        AlterStatement stmt = new AlterStatement();
        return stmt;
    }

    /**
     * Visits a column definition and extracts column information.
     * 
     * @param ctx The column definition context
     * @return A Column object with the extracted information
     */
    private Column extractColumnDef(OpenGaussSQLParser.ColumnDefContext ctx) {
        if (ctx == null || ctx.IDENTIFIER() == null) {
            return null;
        }
        
        Column column = new Column();
        column.setName(ctx.IDENTIFIER().getText());
        
        // Extract data type
        if (ctx.typename() != null) {
            String dataType = ctx.typename().getText();
            column.setDataType(dataType);
        }
        
        return column;
    }

    /**
     * Visits a table constraint and extracts constraint information.
     * 
     * @param ctx The table constraint context
     * @return A Constraint object with the extracted information
     */
    private Constraint extractTableconstraint(OpenGaussSQLParser.TableconstraintContext ctx) {
        if (ctx == null) {
            return null;
        }
        
        Constraint constraint = new Constraint();
        // Basic constraint extraction - can be enhanced later
        constraint.setType("CHECK"); // Default type for now
        
        return constraint;
    }

    /**
     * Visits partitioning information and extracts partition configuration.
     * 
     * @param ctx The partitioning context
     * @return A PartitioningInformation object with the extracted configuration
     */
    private PartitioningInformation extractOptPartitionBy(OpenGaussSQLParser.OptPartitionByContext ctx) {
        if (ctx == null || ctx.partitionStrategy() == null) {
            return null;
        }
        
        // Extract partition type
        PartitionType partitionType = null;
        String strategyText = ctx.partitionStrategy().getText().toUpperCase();
        switch (strategyText) {
            case "RANGE":
                partitionType = PartitionType.RANGE;
                break;
            case "LIST":
                partitionType = PartitionType.LIST;
                break;
            case "HASH":
                partitionType = PartitionType.HASH;
                break;
            default:
                return null;
        }
        
        PartitioningInformation partitioning = new PartitioningInformation(partitionType);
        
        // Extract partition keys
        if (ctx.columnlist() != null && ctx.columnlist().IDENTIFIER() != null) {
            for (org.antlr.v4.runtime.tree.TerminalNode identifier : ctx.columnlist().IDENTIFIER()) {
                if (identifier != null) {
                    partitioning.addPartitionKey(identifier.getText());
                }
            }
        }
        
        // Extract partition definitions if present
        if (ctx.optPartitionByClause() != null) {
            visitOptPartitionByClause(ctx.optPartitionByClause(), partitioning);
        }
        
        return partitioning;
    }

    /**
     * Visits partition by clause and extracts partition definitions.
     * 
     * @param ctx The partition by clause context
     * @param partitioning The partitioning information to populate
     */
    public void visitOptPartitionByClause(OpenGaussSQLParser.OptPartitionByClauseContext ctx, PartitioningInformation partitioning) {
        if (ctx == null) {
            return;
        }
        
        // Handle PARTITIONS count (for HASH partitioning)
        if (ctx.ICONST() != null) {
            // For HASH partitioning with PARTITIONS count, we don't create individual definitions
            // The count is stored in the partitioning information
            return;
        }
        
        // Handle explicit partition definitions
        if (ctx.optPartitionDefElementList() != null && ctx.optPartitionDefElementList().partitionDefElement() != null) {
            for (OpenGaussSQLParser.PartitionDefElementContext partitionDef : ctx.optPartitionDefElementList().partitionDefElement()) {
                PartitionDefinition partition = extractPartitionDefElement(partitionDef);
                if (partition != null) {
                    partitioning.addPartition(partition);
                }
            }
        }
    }

    /**
     * Visits a partition definition element and extracts partition information.
     * 
     * @param ctx The partition definition element context
     * @return A PartitionDefinition object with the extracted information
     */
    private PartitionDefinition extractPartitionDefElement(OpenGaussSQLParser.PartitionDefElementContext ctx) {
        if (ctx == null || ctx.IDENTIFIER() == null) {
            return null;
        }
        
        PartitionDefinition partition = new PartitionDefinition(ctx.IDENTIFIER().getText());
        
        // Extract partition bound specification
        if (ctx.partitionBoundSpec() != null) {
            visitPartitionBoundSpec(ctx.partitionBoundSpec(), partition);
        }
        
        return partition;
    }

    /**
     * Visits partition bound specification and extracts bound information.
     * 
     * @param ctx The partition bound specification context
     * @param partition The partition definition to populate
     */
    public void visitPartitionBoundSpec(OpenGaussSQLParser.PartitionBoundSpecContext ctx, PartitionDefinition partition) {
        if (ctx == null) {
            return;
        }
        
        // Handle VALUES IN (...) for LIST partitions
        if (ctx.IN() != null && ctx.exprList() != null) {
            List<String> values = new ArrayList<>();
            for (OpenGaussSQLParser.ExprContext expr : ctx.exprList().expr()) {
                if (expr != null) {
                    values.add(expr.getText());
                }
            }
            partition.setValues(values);
        }
        // Handle VALUES (...) for RANGE partitions (simplified)
        else if (ctx.exprList() != null) {
            // For RANGE partitions, we currently support simple value lists
            // This can be enhanced to support LESS THAN syntax later
            List<String> values = new ArrayList<>();
            for (OpenGaussSQLParser.ExprContext expr : ctx.exprList().expr()) {
                if (expr != null) {
                    values.add(expr.getText());
                }
            }
            // For RANGE partitions, we can treat the first value as minValue
            if (!values.isEmpty()) {
                partition.setMinValue(values.get(0));
            }
        }
    }

    /**
     * Visits a CREATE FOREIGN TABLE statement and extracts foreign table information.
     * 
     * @param ctx The CREATE FOREIGN TABLE statement context
     * @return A CreateStatement containing the ExternalTable information
     */
    @Override
    public SQLStatement visitCreateForeignTableStmt(OpenGaussSQLParser.CreateForeignTableStmtContext ctx) {
        // Extract table name and server name for constructor
        String tableName = null;
        String serverName = null;
        
        if (ctx.IDENTIFIER(0) != null) {
            tableName = ctx.IDENTIFIER(0).getText();
        }
        
        if (ctx.IDENTIFIER(1) != null) {
            serverName = ctx.IDENTIFIER(1).getText();
        }
        
        // Validate that we have the required information
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new com.sdchat.ogsql.exception.ParseException("Foreign table name cannot be null or empty");
        }
        
        if (serverName == null || serverName.trim().isEmpty()) {
            throw new com.sdchat.ogsql.exception.ParseException("Foreign server name cannot be null or empty");
        }
        
        ExternalTable externalTable = new ExternalTable(tableName, serverName);
        
        // Extract column definitions from foreign table element list
        if (ctx.optforeigntableelementlist() != null && ctx.optforeigntableelementlist().foreigntableelementlist() != null) {
            OpenGaussSQLParser.ForeigntableelementlistContext foreignTableElementList = ctx.optforeigntableelementlist().foreigntableelementlist();
            for (OpenGaussSQLParser.ForeigntableelementContext tableElement : foreignTableElementList.foreigntableelement()) {
                if (tableElement.foreigncolumnDef() != null) {
                    Column column = extractForeignColumnDef(tableElement.foreigncolumnDef());
                    if (column != null) {
                        externalTable.addColumn(column);
                    }
                }
            }
        }
        
        // Extract table options
        if (ctx.optcreateforeigntableoptions() != null) {
            extractForeignTableOptions(ctx.optcreateforeigntableoptions(), externalTable);
        }
        
        // Create a CreateStatement that contains the ExternalTable
        CreateStatement stmt = new CreateStatement();
        stmt.setObjectType("FOREIGN TABLE");
        stmt.setObjectName(externalTable.getTableName());
        
        // Add columns from external table to the statement
        for (Column column : externalTable.getColumns()) {
            stmt.addColumn(column);
        }
        
        // For now, we'll return the ExternalTable directly instead of trying to store it in CreateStatement
        // This is a temporary approach - we'll need to modify CreateStatement to properly support external tables
        return externalTable;
    }

    /**
     * Extracts column definition from foreign table context.
     * 
     * @param ctx The foreign column definition context
     * @return The extracted Column object, or null if extraction fails
     */
    private Column extractForeignColumnDef(OpenGaussSQLParser.ForeigncolumnDefContext ctx) {
        if (ctx == null) {
            return null;
        }
        
        Column column = new Column();
        
        // Extract column name
        if (ctx.IDENTIFIER() != null) {
            column.setName(ctx.IDENTIFIER().getText());
        }
        
        // Extract data type
        if (ctx.typename() != null) {
            column.setDataType(ctx.typename().getText());
        }
        
        // Extract column constraints and options
        if (ctx.foreigncolquallist() != null && ctx.foreigncolquallist().foreigncolqual() != null) {
            for (OpenGaussSQLParser.ForeigncolqualContext colQual : ctx.foreigncolquallist().foreigncolqual()) {
                if (colQual.OPTIONS() != null && colQual.foreignColumnOptionList() != null) {
                    // Handle column-level options
                    extractForeignColumnOptions(colQual.foreignColumnOptionList(), column);
                }
            }
        }
        
        return column;
    }

    /**
     * Extracts foreign table options from the options context.
     * 
     * @param ctx The options context
     * @param externalTable The external table to populate with options
     */
    private void extractForeignTableOptions(OpenGaussSQLParser.OptcreateforeigntableoptionsContext ctx, ExternalTable externalTable) {
        if (ctx == null) {
            return;
        }
        
        // Extract table options
        if (ctx.OPTIONS() != null && ctx.createForeignTableOptionList() != null) {
            for (OpenGaussSQLParser.CreateForeignTableOptionContext option : ctx.createForeignTableOptionList().createForeignTableOption()) {
                if (option.SCONST(0) != null && option.SCONST(1) != null) {
                    String key = option.SCONST(0).getText().replace("'", "");
                    String value = option.SCONST(1).getText().replace("'", "");
                    externalTable.addTableOption(key, value);
                }
            }
        }
        
        // Extract server options
        if (ctx.optServerOptions() != null && ctx.optServerOptions().SERVER() != null 
            && ctx.optServerOptions().OPTIONS() != null && ctx.optServerOptions().createForeignTableOptionList() != null) {
            for (OpenGaussSQLParser.CreateForeignTableOptionContext option : ctx.optServerOptions().createForeignTableOptionList().createForeignTableOption()) {
                if (option.SCONST(0) != null && option.SCONST(1) != null) {
                    String key = option.SCONST(0).getText().replace("'", "");
                    String value = option.SCONST(1).getText().replace("'", "");
                    externalTable.addServerOption(key, value);
                }
            }
        }
    }

    /**
     * Extracts foreign column options from the options context.
     * 
     * @param ctx The foreign column option list context
     * @param column The column to populate with options
     */
    private void extractForeignColumnOptions(OpenGaussSQLParser.ForeignColumnOptionListContext ctx, Column column) {
        if (ctx == null) {
            return;
        }
        
        for (OpenGaussSQLParser.ForeignColumnOptionContext option : ctx.foreignColumnOption()) {
            if (option.SCONST(0) != null && option.SCONST(1) != null) {
                String key = option.SCONST(0).getText().replace("'", "");
                String value = option.SCONST(1).getText().replace("'", "");
                // Store column options in the column's options field
                // For now, we'll append to existing options if any
                String existingOptions = column.getOptions();
                String newOption = key + "=" + value;
                if (existingOptions == null || existingOptions.isEmpty()) {
                    column.setOptions(newOption);
                } else {
                    column.setOptions(existingOptions + ", " + newOption);
                }
            }
        }
    }

    @Override
    public SQLStatement visitCreateprocedurestmt(OpenGaussSQLParser.CreateprocedurestmtContext ctx) {
        if (ctx == null || ctx.IDENTIFIER().isEmpty()) {
            return null;
        }

        String procedureName = ctx.IDENTIFIER(0).getText();
        boolean orReplace = ctx.OR() != null && ctx.REPLACE() != null;

        CreateProcedureStmt stmt = new CreateProcedureStmt(procedureName, orReplace);

        if (ctx.optparameterlist() != null && ctx.optparameterlist().parameterlist() != null) {
            OpenGaussSQLParser.ParameterlistContext paramList = ctx.optparameterlist().parameterlist();
            for (OpenGaussSQLParser.ParameterdefContext paramDef : paramList.parameterdef()) {
                if (paramDef != null) {
                    ProcedureParameter param = extractParameterDef(paramDef);
                    if (param != null) {
                        stmt.addParameter(param);
                    }
                }
            }
        }

        String language = "plpgsql";
        if (ctx.LANGUAGE(0) != null) {
            language = ctx.LANGUAGE(0).getText();
        } else if (ctx.LANGUAGE().size() > 1) {
            language = ctx.LANGUAGE(1).getText();
        }

         if (ctx.procedureBody().dolString() != null) {
            ProcedureBody body = new ProcedureBody(language, ctx.procedureBody().dolString().getText());
            stmt.setBody(body);
         } else if (ctx.procedureBody().BEGIN() != null) {
            String procLanguage = ctx.LANGUAGE() != null && ctx.LANGUAGE().size() > 0 ? ctx.LANGUAGE(0).getText() : "plpgsql";
            ProcedureBody body = new ProcedureBody(procLanguage, ctx.procedureBody().BEGIN().getText() + " " + ctx.procedureBody().END().getText());
            stmt.setBody(body);
        }

        if (ctx.SECURITY() != null && ctx.DEFINER() != null) {
            ProcedureSecurity security = new ProcedureSecurity();
            security.setDefiner(true);
            stmt.setSecurity(security);
        }

        // VARIADIC parameter must be the last parameter
        if (stmt.hasParameters()) {
            for (int i = 0; i < stmt.getParameters().size() - 1; i++) {
                ProcedureParameter param = stmt.getParameters().get(i);
                if (param.getMode() == ProcedureParameter.ParameterMode.VARIADIC) {
                    throw new SemanticErrorException(
                        "VARIADIC parameter must be the last parameter",
                        ctx.start.getLine(),
                        "VARIADIC parameter at position " + i + " must be last"
                    );
                }
            }
        }

        return stmt;
    }

    private ProcedureParameter extractParameterDef(OpenGaussSQLParser.ParameterdefContext ctx) {
        if (ctx == null || ctx.IDENTIFIER() == null) {
            return null;
        }

        String paramName = ctx.IDENTIFIER().getText();
        ProcedureParameter.ParameterMode mode = ProcedureParameter.ParameterMode.IN;
        String dataType = "";

        if (ctx.IN() != null) {
            mode = ProcedureParameter.ParameterMode.IN;
        } else if (ctx.OUT() != null) {
            mode = ProcedureParameter.ParameterMode.OUT;
        } else if (ctx.INOUT() != null) {
            mode = ProcedureParameter.ParameterMode.INOUT;
        }

        if (ctx.typename() != null) {
            dataType = ctx.typename().getText();
        }

        return new ProcedureParameter(paramName, mode, dataType);
    }

    public <T> T visit(ExternalTable externalTable) {
        // Default implementation - can be overridden by specific visitors
        return null;
    }

    @Override
    public SQLStatement visitAlterprocedurestmt(OpenGaussSQLParser.AlterprocedurestmtContext ctx) {
        if (ctx == null || ctx.IDENTIFIER() == null) {
            return null;
        }

        String procedureName = ctx.IDENTIFIER().get(0).getText();
        AlterProcedureStmt stmt = new AlterProcedureStmt(procedureName);

        if (ctx.RENAME() != null && ctx.IDENTIFIER().size() > 1) {
            stmt.setNewName(ctx.IDENTIFIER(1).getText());
        } else if (ctx.OWNER() != null && ctx.IDENTIFIER().size() > 1) {
            stmt.setNewOwner(ctx.IDENTIFIER(1).getText());
        } else if (ctx.SET() != null && ctx.SCHEMA() != null && ctx.IDENTIFIER().size() > 1) {
            stmt.setNewSchema(ctx.IDENTIFIER(1).getText());
        } else if (ctx.SECURITY() != null && ctx.INVOKER() != null) {
            stmt.setSecurityInvoker(Boolean.TRUE);
        }

        return stmt;
    }

    @Override
    public SQLStatement visitCallstmt(OpenGaussSQLParser.CallstmtContext ctx) {
        if (ctx == null || ctx.IDENTIFIER() == null) {
            return null;
        }

        String procedureName = ctx.IDENTIFIER().getText();
        CallFuncStmt stmt = new CallFuncStmt(procedureName);

        if (ctx.callArguments() != null) {
            OpenGaussSQLParser.CallArgumentsContext callArgs = ctx.callArguments();
            
            boolean hasNamedArgs = false;
            boolean hasPositionalArgs = false;

            if (callArgs.callArg() != null) {
                for (OpenGaussSQLParser.CallArgContext argCtx : callArgs.callArg()) {
                    if (argCtx.IDENTIFIER() != null && argCtx.ARROW() != null) {
                        if (hasPositionalArgs) {
                            throw new SemanticErrorException(
                                "CALL statement cannot mix positional and named arguments",
                                ctx.start.getLine(),
                                "Mixed argument styles in CALL: use either positional or named arguments, not both"
                            );
                        }
                        String paramName = argCtx.IDENTIFIER().getText();
                        ValueExpression valueExpr = new ValueExpression(argCtx.aexpr().getText());
                        stmt.addNamedArgument(paramName, valueExpr);
                        hasNamedArgs = true;
                    } else if (argCtx.aexpr() != null) {
                        if (hasNamedArgs) {
                            throw new SemanticErrorException(
                                "CALL statement cannot mix positional and named arguments",
                                ctx.start.getLine(),
                                "Mixed argument styles in CALL: use either positional or named arguments, not both"
                            );
                        }
                        ValueExpression valueExpr = new ValueExpression(argCtx.aexpr().getText());
                        stmt.addArgument(valueExpr);
                        hasPositionalArgs = true;
                    }
                }
            }
        }

        return stmt;
    }

    @Override
    public SQLStatement visitSetstmt(OpenGaussSQLParser.SetstmtContext ctx) {
        SetStatement stmt = new SetStatement();

        if (ctx.IDENTIFIER() != null) {
            stmt.setParameterName(ctx.IDENTIFIER().getText());
        }

        if (ctx.aexpr() != null) {
            stmt.setValue(new ValueExpression(ctx.aexpr().getText()));
        }

        return stmt;
    }

    @Override
    public SQLStatement visitCreateschemastmt(OpenGaussSQLParser.CreateschemastmtContext ctx) {
        CreateSchemaStatement stmt = new CreateSchemaStatement();

        if (ctx.IDENTIFIER() != null) {
            stmt.setSchemaName(ctx.IDENTIFIER().getText());
        }

        return stmt;
    }
}
