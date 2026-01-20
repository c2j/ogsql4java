# Quickstart: Gauss Stored Procedure Parser

**Feature**: Gauss Stored Procedure Parsing Enhancement
**Version**: 1.0.0
**Date**: 2025-01-15

## Overview

This quickstart guide helps developers get started with the Gauss stored procedure parsing capabilities in the OpenGauss SQL Parser. It covers basic usage, common patterns, and examples.

---

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- ANTLR4 4.13.1 (included as dependency)
- Git (for cloning the repository)

---

## Setup

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/ogsql4java.git
cd ogsql4java
```

### 2. Generate ANTLR4 Parser

```bash
mvn clean generate-sources
```

This generates Java source files from the ANTLR4 grammar in `src/main/java/com/sdchat/ogsql/grammar/OpenGaussSQL.g4`.

### 3. Build the Project

```bash
mvn clean compile
```

### 4. Run Tests

```bash
mvn test
```

### 5. Verify Installation

```bash
mvn exec:java -Dexec.mainClass="com.sdchat.ogsql.parser.SQLParser"
```

---

## Basic Usage

### Parse a CREATE PROCEDURE Statement

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.CreateProcedureStmt;
import com.sdchat.ogsql.exception.ParseException;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        String sql = """
            CREATE OR REPLACE PROCEDURE calculate_bonus(
                p_employee_id IN INTEGER,
                p_bonus_percent IN NUMERIC DEFAULT 0.10,
                p_bonus_amount OUT NUMERIC
            )
            LANGUAGE plpgsql
            SECURITY DEFINER
            AS $$
            BEGIN
                SELECT salary * p_bonus_percent INTO p_bonus_amount
                FROM employees
                WHERE employee_id = p_employee_id;
            END;
            $$;
            """;

        try {
            CreateProcedureStmt stmt = parser.parseCreateProcedure(sql);

            System.out.println("Procedure Name: " + stmt.getProcedureName());
            System.out.println("Parameters: " + stmt.getParameters().size());
            System.out.println("Language: " + stmt.getLanguage());
            System.out.println("Security: " + stmt.getSecurity().isDefiner());

        } catch (ParseException e) {
            System.err.println("Parse error at line " + e.getLineNumber() + ": " + e.getMessage());
        }
    }
}
```

### Parse a CALL Statement

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.CallFuncStmt;
import com.sdchat.ogsql.exception.ParseException;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        String sql = "CALL calculate_bonus(12345, 0.15, NULL);";

        try {
            CallFuncStmt stmt = parser.parseCallStatement(sql);

            System.out.println("Calling: " + stmt.getProcedureName());
            System.out.println("Arguments: " + stmt.getArguments().size());

        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getMessage());
        }
    }
}
```

### Parse with Named Parameters

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.CallFuncStmt;
import com.sdchat.ogsql.exception.ParseException;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        String sql = "CALL calculate_bonus(p_employee_id => 12345, p_bonus_percent => 0.15, p_bonus_amount => NULL);";

        try {
            CallFuncStmt stmt = parser.parseCallStatement(sql);

            System.out.println("Calling: " + stmt.getProcedureName());
            System.out.println("Named Arguments: " + stmt.getNamedArguments().size());

        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getMessage());
        }
    }
}
```

### Parse ALTER PROCEDURE

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.AlterProcedureStmt;
import com.sdchat.ogsql.exception.ParseException;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        String sql = "ALTER PROCEDURE calculate_bonus RENAME TO calc_emp_bonus;";

        try {
            AlterProcedureStmt stmt = parser.parseAlterProcedure(sql);

            System.out.println("Altering: " + stmt.getProcedureName());
            System.out.println("New Name: " + stmt.getNewName());

        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getMessage());
        }
    }
}
```

### Parse DROP PROCEDURE

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.DropStmt;
import com.sdchat.ogsql.exception.ParseException;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        String sql = "DROP PROCEDURE IF EXISTS calculate_bonus CASCADE;";

        try {
            DropStmt stmt = parser.parseDropProcedure(sql);

            System.out.println("Dropping: " + stmt.getObjectName());

        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getMessage());
        }
    }
}
```

---

## Using the Visitor Pattern

### Implement a Custom Visitor

```java
import com.sdchat.ogsql.ast.*;
import com.sdchat.ogsql.ast.visitor.ASTVisitor;

public class ProcedurePrinter implements ASTVisitor<String> {

    @Override
    public String visitCreateProcedureStmt(CreateProcedureStmt stmt) {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE");
        if (stmt.isOrReplace()) {
            sb.append(" OR REPLACE");
        }
        sb.append(" PROCEDURE ").append(stmt.getProcedureName());
        sb.append("(\n");

        // Print parameters
        List<ProcedureParameter> params = stmt.getParameters();
        for (int i = 0; i < params.size(); i++) {
            ProcedureParameter param = params.get(i);
            sb.append("    ").append(param.getName());
            sb.append(" ").append(param.getMode());
            sb.append(" ").append(param.getDataType());

            if (param.getDefaultValue() != null) {
                sb.append(" DEFAULT ").append(param.getDefaultValue());
            }

            if (i < params.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(")\n");
        sb.append("LANGUAGE ").append(stmt.getLanguage()).append("\n");

        // Print security
        if (stmt.getSecurity().isDefiner()) {
            sb.append("SECURITY DEFINER\n");
        }

        sb.append("AS ");
        sb.append(stmt.getBody().getSourceCode());

        return sb.toString();
    }

    @Override
    public String visitAlterProcedureStmt(AlterProcedureStmt stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("ALTER PROCEDURE ").append(stmt.getProcedureName());

        if (stmt.getNewName() != null) {
            sb.append(" RENAME TO ").append(stmt.getNewName());
        } else if (stmt.getNewOwner() != null) {
            sb.append(" OWNER TO ").append(stmt.getNewOwner());
        } else if (stmt.getNewSchema() != null) {
            sb.append(" SET SCHEMA ").append(stmt.getNewSchema());
        } else if (Boolean.TRUE.equals(stmt.getSecurityInvoker())) {
            sb.append(" SECURITY INVOKER");
        }

        sb.append(";");

        return sb.toString();
    }

    @Override
    public String visitCallFuncStmt(CallFuncStmt stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("CALL ").append(stmt.getProcedureName());
        sb.append("(");

        List<ValueExpression> args = stmt.getArguments();
        Map<String, ValueExpression> namedArgs = stmt.getNamedArguments();

        if (!args.isEmpty()) {
            // Positional arguments
            for (int i = 0; i < args.size(); i++) {
                sb.append(args.get(i));
                if (i < args.size() - 1) {
                    sb.append(", ");
                }
            }
        } else if (!namedArgs.isEmpty()) {
            // Named arguments
            int i = 0;
            for (Map.Entry<String, ValueExpression> entry : namedArgs.entrySet()) {
                sb.append(entry.getKey()).append(" => ").append(entry.getValue());
                if (i < namedArgs.size() - 1) {
                    sb.append(", ");
                }
                i++;
            }
        }

        sb.append(");");

        return sb.toString();
    }

    // Other visit methods...
}
```

### Use the Visitor

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.CreateProcedureStmt;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        try {
            CreateProcedureStmt stmt = parser.parseCreateProcedure(sql);
            ProcedurePrinter printer = new ProcedurePrinter();

            // Generate SQL from AST
            String generatedSQL = stmt.accept(printer);
            System.out.println("Generated SQL:\n" + generatedSQL);

        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getMessage());
        }
    }
}
```

---

## Error Handling

### Handle Syntax Errors

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.exception.SyntaxErrorException;
import com.sdchat.ogsql.exception.ParseException;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        // Missing closing parenthesis
        String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        try {
            parser.parseCreateProcedure(sql);
        } catch (SyntaxErrorException e) {
            System.err.println("Syntax error:");
            System.err.println("  Line: " + e.getLineNumber());
            System.err.println("  Column: " + e.getColumnNumber());
            System.err.println("  Expected: " + e.getExpectedToken());
            System.err.println("  Found: " + e.getFoundToken());
            System.err.println("  Message: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getMessage());
        }
    }
}
```

### Handle Semantic Errors

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.exception.SemanticErrorException;
import com.sdchat.ogsql.exception.ParseException;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        // VARIADIC parameter not last
        String sql = "CREATE PROCEDURE test_proc(p1 VARIADIC INTEGER[], p2 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        try {
            parser.parseCreateProcedure(sql);
        } catch (SemanticErrorException e) {
            System.err.println("Semantic error:");
            System.err.println("  Issue: " + e.getSemanticIssue());
            System.err.println("  Line: " + e.getLineNumber());
            System.err.println("  Message: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getMessage());
        }
    }
}
```

---

## Configuration

### Set Compatibility Mode

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.ParserConfiguration;

public class Example {
    public static void main(String[] args) {
        ParserConfiguration config = new ParserConfiguration();
        config.setDefaultCompatibilityMode("A");  // Oracle compatibility mode

        SQLParser parser = new SQLParser(config);

        String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER) AS BEGIN NULL; END;";

        // Parse with Oracle compatibility mode
        // ...
    }
}
```

### Set Size Limits

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.ParserConfiguration;

public class Example {
    public static void main(String[] args) {
        ParserConfiguration config = new ParserConfiguration();
        config.setMaxProcedureBodySize(2_097_152);  // 2 MB limit
        config.setMaxProcedureNestingDepth(200);  // 200 levels

        SQLParser parser = new SQLParser(config);

        // Parse with custom limits
        // ...
    }
}
```

---

## Common Patterns

### Extract Procedure Metadata

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.CreateProcedureStmt;
import com.sdchat.ogsql.ast.ProcedureParameter;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        String sql = """
            CREATE OR REPLACE PROCEDURE complex_proc(
                p1 IN INTEGER DEFAULT 0,
                p2 INOUT NUMERIC,
                p3 OUT VARCHAR
            )
            LANGUAGE plpgsql
            SECURITY DEFINER
            AS $$
            BEGIN NULL; END;
            $$;
            """;

        try {
            CreateProcedureStmt stmt = parser.parseCreateProcedure(sql);

            // Extract metadata
            System.out.println("Procedure Name: " + stmt.getProcedureName());
            System.out.println("OR REPLACE: " + stmt.isOrReplace());
            System.out.println("Language: " + stmt.getLanguage());
            System.out.println("Security DEFINER: " + stmt.getSecurity().isDefiner());
            System.out.println("\nParameters:");

            List<ProcedureParameter> params = stmt.getParameters();
            for (ProcedureParameter param : params) {
                System.out.println("  - " + param.getName() +
                    " (" + param.getMode() + " " + param.getDataType() + ")");
                if (param.getDefaultValue() != null) {
                    System.out.println("    Default: " + param.getDefaultValue());
                }
            }

        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getMessage());
        }
    }
}
```

### Validate Procedure Before Execution

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.CreateProcedureStmt;
import com.sdchat.ogsql.ast.ProcedureParameter;
import com.sdchat.ogsql.ast.ProcedureParameter.ParameterMode;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        SQLParser parser = new SQLParser();

        String sql = "...";

        try {
            CreateProcedureStmt stmt = parser.parseCreateProcedure(sql);

            // Validate
            List<ProcedureParameter> params = stmt.getParameters();
            for (ProcedureParameter param : params) {
                // Check VARIADIC is last
                if (param.getMode() == ParameterMode.VARIADIC) {
                    int lastIndex = params.size() - 1;
                    if (params.get(lastIndex) != param) {
                        System.err.println("Error: VARIADIC parameter must be last");
                        return;
                    }
                }
            }

            System.out.println("Procedure validation successful!");

        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getMessage());
        }
    }
}
```

### Generate Documentation from Procedures

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.CreateProcedureStmt;
import com.sdchat.ogsql.ast.ProcedureParameter;
import java.util.List;

public class ProcedureDocGenerator implements ASTVisitor<String> {

    @Override
    public String visitCreateProcedureStmt(CreateProcedureStmt stmt) {
        StringBuilder sb = new StringBuilder();

        sb.append("## ").append(stmt.getProcedureName()).append("\n\n");
        sb.append("**Language**: ").append(stmt.getLanguage()).append("\n\n");
        sb.append("**Security**: ");
        if (stmt.getSecurity().isDefiner()) {
            sb.append("DEFINER");
        } else {
            sb.append("INVOKER");
        }
        sb.append("\n\n");

        sb.append("### Parameters\n\n");
        sb.append("| Name | Mode | Type | Default |\n");
        sb.append("|------|------|------|---------|\n");

        List<ProcedureParameter> params = stmt.getParameters();
        for (ProcedureParameter param : params) {
            sb.append("| ").append(param.getName());
            sb.append(" | ").append(param.getMode());
            sb.append(" | ").append(param.getDataType());
            sb.append(" | ").append(param.getDefaultValue() != null ? param.getDefaultValue() : "");
            sb.append(" |\n");
        }

        sb.append("\n### Body\n\n");
        sb.append("```\n");
        sb.append(stmt.getBody().getSourceCode());
        sb.append("\n```\n");

        return sb.toString();
    }

    // Other visit methods...
}
```

---

## Testing Your Code

### Write Unit Tests

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.CreateProcedureStmt;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyProcedureTest {

    @Test
    public void testParseSimpleProcedure() throws Exception {
        SQLParser parser = new SQLParser();
        String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";

        CreateProcedureStmt stmt = parser.parseCreateProcedure(sql);

        assertEquals("test_proc", stmt.getProcedureName());
        assertEquals(1, stmt.getParameters().size());
        assertEquals("plpgsql", stmt.getLanguage());
    }
}
```

### Run Tests with Coverage

```bash
mvn clean test jacoco:report
```

View coverage report at `target/site/jacoco/index.html`.

---

## Troubleshooting

### Common Issues

**Issue**: "OpenGaussSQLLexer cannot be resolved to a type"
- **Solution**: Run `mvn clean generate-sources` to generate ANTLR4 parser files

**Issue**: "Grammar error at line X"
- **Solution**: Check the ANTLR4 grammar file `OpenGaussSQL.g4` for syntax errors

**Issue**: "Procedure not found" semantic error
- **Solution**: This is a validation error, not a parsing error. The procedure may not exist in the database context

**Issue**: Slow parsing performance
- **Solution**: Increase `maxProcedureBodySize` limit or simplify procedure bodies. Use `mvn test` with profiling to identify bottlenecks

---

## Next Steps

- Read the full API contract at `contracts/api-contract.md`
- Review the data model at `data-model.md`
- Explore test examples in `contracts/test-contract.md`
- Check the grammar specification in `contracts/grammar-contract.md`

---

## Resources

- [ANTLRR4 Documentation](https://www.antlr.org/)
- [OpenGauss Documentation](https://opengauss.org/)
- [PL/pgSQL Reference](https://www.postgresql.org/docs/current/plpgsql.html)
- Feature Specification: `specs/003-gauss-procedure-parsing/spec.md`
- Implementation Plan: `specs/003-gauss-procedure-parsing/plan.md`
