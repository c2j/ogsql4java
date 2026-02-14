// OpenGauss SQL Grammar
// Version: 1.0.0
// Based on PostgreSQL ANTLR4 grammar from antlr/grammars-v4 repository
// Extended with OpenGauss-specific features: Hints, Partitioning, Foreign Tables

grammar OpenGaussSQL;

@header {
package com.sdchat.ogsql.grammar;
}

// ==================== Parser Rules ====================

// Root rule for parsing SQL
root
    : stmtblock EOF
    ;

// Block of statements
stmtblock
    : stmtmulti
    ;

// Multiple statements
stmtmulti
    : stmt? (SEMI stmt?)*
    ;

// Statement types (MVP subset - will be expanded)
stmt
    : selectstmt
    | insertstmt
    | updatestmt
    | deletestmt
    | createstmt
    | createschemastmt
    | createprocedurestmt
    | createfunctionstmt
    | createsequencestmt
    | createindexstmt
    | createtypestmt
    | createpackagestmt
    | createuserstmt
    | createdbstmt
    | createForeignTableStmt
    | alterprocedurestmt
    | alterstmt
    | dropstmt
    | dropdatabasestmt
    | callstmt
    | setstmt
    | savepointstmt
    | releasesavepointstmt
    | rollbackstmt
    | commitstmt
    | showstmt
    | explainstmt
    | analyzestmt
    | truncatestmt
    | startTransactionstmt
    | cursorstmt
    | fetchstmt
    ;

// ==================== WITH Clause (Common Table Expressions) ====================

withClause
    : WITH RECURSIVE? commonTableExpr (',' commonTableExpr)*
    ;

commonTableExpr
    : IDENTIFIER '(' IDENTIFIER (',' IDENTIFIER)* ')' AS '(' selectstmt ')'
    | IDENTIFIER AS '(' selectstmt ')'
    ;

// ==================== SELECT Statement ====================

selectstmt
    : withClause? SELECT hintComment? optdistinct selectlist fromclause whereclause groupbyclause havingclause orderbyclause limitclause
    | selectstmt UNION (ALL | DISTINCT)? selectstmt
    | selectstmt EXCEPT (ALL | DISTINCT)? selectstmt
    | selectstmt INTERSECT (ALL | DISTINCT)? selectstmt
    ;

hintComment
    : HINT_COMMENT
    |
    ;

optdistinct
    : DISTINCT
    | ALL
    |
    ;

selectlist
    : expr (',' expr)*
    | MULT_OP
    ;

// ==================== FROM Clause ====================

fromclause
    : FROM fromExpression
    |
    ;

fromExpression
    : tableExpression (joinExpression)*
    | tableExpression (',' tableExpression)*
    |
    ;

tableExpression
    : tableReference optalias
    ;

tableReference
    : IDENTIFIER
    | '(' selectstmt ')' 
    ;

joinExpression
    : joinType tableExpression joinCondition?
    ;

joinType
    : JOIN
    | INNER JOIN
    | LEFT JOIN
    | RIGHT JOIN
    | FULL JOIN
    | FULL OUTER JOIN
    | LEFT OUTER JOIN
    | RIGHT OUTER JOIN
    | CROSS JOIN
    ;

joinCondition
    : ON aexpr
    | USING '(' columnlist ')'
    ;

optalias
    : AS IDENTIFIER
    | IDENTIFIER
    |
    ;

// ==================== WHERE Clause ====================

whereclause
    : WHERE aexpr
    |
    ;

// ==================== GROUP BY Clause ====================

groupbyclause
    : GROUP BY expr (',' expr)*
    |
    ;

// ==================== HAVING Clause ====================

havingclause
    : HAVING aexpr
    |
    ;

// ==================== ORDER BY Clause ====================

orderbyclause
    : ORDER BY sortby (',' sortby)*
    |
    ;

sortby
    : expr optascdescoptnullsorder
    ;

optascdescoptnullsorder
    : ASC optnullsorder
    | DESC optnullsorder
    |
    ;

optnullsorder
    : NULLS FIRST
    | NULLS LAST
    |
    ;

// ==================== LIMIT Clause ====================

limitclause
    : LIMIT selectlimitvalue offsetclause
    | offsetclause
    |
    ;

selectlimitvalue
    : aexpr
    | ALL
    ;

selectoffsetvalue
    : OFFSET selectoffsetvalue2
    ;

selectoffsetvalue2
    : aexpr
    ;

offsetclause
    : OFFSET selectoffsetvalue2
    |
    ;

// ==================== INSERT Statement ====================

insertstmt
    : INSERT INTO IDENTIFIER optcolumnList insertrest onConflictClause?
    ;

optcolumnList
    : '(' columnlist ')'
    | '(' selectstmt ')'
    |
    ;

columnlist
    : IDENTIFIER (',' IDENTIFIER)*
    ;

insertrest
    : VALUES inserttargetlist (',' inserttargetlist)* returningClause?
    | selectstmt returningClause?
    ;

inserttargetlist
    : '(' inserttargetel (',' inserttargetel)* ')'
    ;

inserttargetel
    : aexpr
    | DEFAULT
    ;

onConflictClause
    : ON CONFLICT conflictTarget? conflictAction
    | ON DUPLICATE KEY UPDATE setclist
    ;

conflictTarget
    : '(' columnlist ')'
    | ON CONSTRAINT IDENTIFIER
    |
    ;

conflictAction
    : DO NOTHING
    | DO UPDATE SET setclist whereclause?
    ;

returningClause
    : RETURNING returningExpressionList
    ;

returningExpressionList
    : returningExpression (',' returningExpression)*
    ;

returningExpression
    : qualifiedIdentifier
    | '*' AS IDENTIFIER
    | '*'
    | aexpr AS IDENTIFIER
    | aexpr
    ;

// ==================== UPDATE Statement ====================

updatestmt
    : UPDATE IDENTIFIER optalias SET setclist fromclause whereclause returningClause?
    ;

setclist
    : settarget (',' settarget)*
    ;

settarget
    : IDENTIFIER '=' aexpr
    | IDENTIFIER '.' IDENTIFIER '=' aexpr
    ;

// ==================== DELETE Statement ====================

deletestmt
    : DELETE (FROM)? IDENTIFIER optalias whereclause returningClause?
    ;

// ==================== CREATE Statement ====================

createstmt
    : CREATE TABLE IDENTIFIER '(' opttableelementlist ')' optPartitionBy optDistributeBy optWithClause
    | createForeignTableStmt
    ;

optWithClause
    : WITH '(' tableWithOption (',' tableWithOption)* ')'
    |
    ;

tableWithOption
    : IDENTIFIER '=' (IDENTIFIER | ICONST | SCONST)
    | IDENTIFIER
    ;

optDistributeBy
    : DISTRIBUTE BY (HASH | REPLICATE) '(' columnlist ')'
    | DISTRIBUTE BY (HASH | REPLICATE) '(' columnlist ')' optPartitionBy
    |
    ;

// ==================== CREATE PROCEDURE Statement ====================

createprocedurestmt
    : CREATE (OR REPLACE)? PROCEDURE IDENTIFIER '(' optparameterlist ')' 
      (LANGUAGE IDENTIFIER)?
      (SECURITY DEFINER)?
      (AS | IS) procedureBody
      (LANGUAGE IDENTIFIER)?
      (SEMI)?
    ;

procedureBody
    : dolString
    | BEGIN statementList END
    ;

statementList
    : stmt (SEMI stmt)* SEMI?
    ;

alterprocedurestmt
    : ALTER PROCEDURE IDENTIFIER (
      RENAME TO IDENTIFIER
      | OWNER TO IDENTIFIER
      | SET SCHEMA IDENTIFIER
      | SECURITY INVOKER
      )
      (SEMI)?
    ;

// ==================== CALL Statement ====================

callstmt
    : CALL IDENTIFIER callArguments (SEMI)?
    ;

callArguments
    : '(' (callArg (',' callArg)*)? ')'
    ;

callArg
    : aexpr
    | IDENTIFIER ARROW aexpr
    ;

setstmt
    : SET IDENTIFIER (TO | EQ) aexpr (SEMI)?
    ;

savepointstmt
    : SAVEPOINT savepointId (SEMI)?
    ;

releasesavepointstmt
    : RELEASE SAVEPOINT? savepointId (SEMI)?
    ;

rollbackstmt
    : ROLLBACK WORK? TO SAVEPOINT? savepointId (SEMI)?
    | ROLLBACK WORK? (SEMI)?
    ;

savepointId
    : IDENTIFIER
    | QIDENT
    ;

commitstmt
    : COMMIT WORK? (SEMI)?
    ;

startTransactionstmt
    : START TRANSACTION transactionModeList?
    | BEGIN (transactionModeList)?
    ;

transactionModeList
    : transactionMode (',' transactionMode)*
    ;

transactionMode
    : ISOLATION LEVEL isolationLevel
    | READ ONLY
    | READ WRITE
    | NOT? DEFERRABLE
    ;

isolationLevel
    : READ UNCOMMITTED
    | READ COMMITTED
    | REPEATABLE READ
    | SERIALIZABLE
    ;

cursorstmt
    : CURSOR IDENTIFIER (SCROLL | NO SCROLL)? FOR selectstmt
    ;

fetchstmt
    : FETCH (NEXT | PRIOR | FIRST | LAST | (IDENTIFIER | ALL | FORWARD | BACKWARD) (INT | ALL)?) FROM? IDENTIFIER
    ;

showstmt
    : SHOW (ALL | IDENTIFIER)
    ;

explainstmt
    : EXPLAIN ((ANALYZE | VERBOSE | COSTS | BUFFERS | TIMING | FORMAT IDENTIFIER) (',' (ANALYZE | VERBOSE | COSTS | BUFFERS | TIMING | FORMAT IDENTIFIER))*)?
      (selectstmt | insertstmt | updatestmt | deletestmt | callstmt)
    ;

createschemastmt
    : CREATE SCHEMA IDENTIFIER (SEMI)?
    ;

optparameterlist
    : parameterlist
    |
    ;

parameterlist
    : parameterdef (',' parameterdef)*
    ;

parameterdef
    : IDENTIFIER (IN | OUT | INOUT)? typename (DEFAULT aexpr)?
    ;

dolString
    : '$$' ( ~'$' | '$' ~'$' )* '$$'
    ;

opttableelementlist
    : tableelementlist
    |
    ;

tableelementlist
    : tableelement (',' tableelement)*
    ;

tableelement
    : columnDef
    | tableconstraint
    ;

columnDef
    : IDENTIFIER typename colquallist
    ;

typename
    : IDENTIFIER
    | IDENTIFIER '(' ICONST ')'
    | IDENTIFIER '(' ICONST ',' ICONST ')'
    | SERIAL
    | BIGSERIAL
    | SMALLSERIAL
    | SERIAL '(' ICONST ')'
    | BIGSERIAL '(' ICONST ')'
    | SMALLSERIAL '(' ICONST ')'
    ;

colquallist
    : colconstraint (',' colconstraint)*
    |
    ;

colconstraint
    : NOT NULL_P
    | NULL_P
    | DEFAULT aexpr
    | UNIQUE
    | PRIMARY KEY
    ;

tableconstraint
    : CONSTRAINT IDENTIFIER constraintElem
    | constraintElem
    ;

constraintElem
    : UNIQUE '(' columnlist ')'
    | PRIMARY KEY '(' columnlist ')'
    | CHECK '(' aexpr ')'
    ;

// ==================== Partitioning (OpenGauss) ====================

optPartitionBy
    : PARTITION BY partitionStrategy '(' columnlist ')' optPartitionByClause
    |
    ;

partitionStrategy
    : RANGE
    | LIST
    | HASH
    ;

optPartitionByClause
    : PARTITIONS ICONST
    | '(' optPartitionDefElementList ')'
    | SUBPARTITION BY partitionStrategy '(' columnlist ')' optSubpartitionByClause
    |
    ;

optPartitionDefElementList
    : partitionDefElement (',' partitionDefElement)?
    |
    ;

partitionDefElement
    : PARTITION IDENTIFIER VALUES partitionBoundSpec
    ;

optSubpartitionByClause
    : SUBPARTITIONS ICONST
    | '(' optSubpartitionDefElementList ')'
    |
    ;

optSubpartitionDefElementList
    : subpartitionDefElement (',' subpartitionDefElement)?
    |
    ;

subpartitionDefElement
    : SUBPARTITION IDENTIFIER VALUES partitionBoundSpec
    ;

partitionBoundSpec
    : IN '(' exprList ')'
    | LESS THAN '(' exprList ')'
    | '(' exprList ')'
    ;

exprList
    : expr (',' expr)*
    ;

// ==================== DROP Statement ====================

dropstmt
    : DROP TABLE (IF EXISTS)? IDENTIFIER (CASCADE | RESTRICT)?
    | DROP PROCEDURE (IF EXISTS)? IDENTIFIER (CASCADE | RESTRICT)?
    | DROP SCHEMA (IF EXISTS)? IDENTIFIER (CASCADE | RESTRICT)?
    | DROP INDEX (IF EXISTS)? IDENTIFIER
    | DROP TYPE (IF EXISTS)? IDENTIFIER
    ;

dropdatabasestmt
    : DROP DATABASE (IF EXISTS)? IDENTIFIER
    ;

// ==================== CREATE INDEX Statement ====================

createindexstmt
    : CREATE (UNIQUE)? INDEX (IF NOT EXISTS)? IDENTIFIER ON IDENTIFIER '(' indexcolumnlist ')'
    ;

indexcolumnlist
    : IDENTIFIER (',' IDENTIFIER)*
    ;

// ==================== CREATE TYPE Statement ====================

createtypestmt
    : CREATE TYPE IDENTIFIER
    ;

// ==================== CREATE FUNCTION Statement ====================

createfunctionstmt
    : CREATE (OR REPLACE)? FUNCTION IDENTIFIER '(' ')' RETURNS typename LANGUAGE IDENTIFIER
    ;

// ==================== CREATE PACKAGE Statement ====================

createpackagestmt
    : CREATE (OR REPLACE)? PACKAGE IDENTIFIER
    ;

// ==================== CREATE SEQUENCE Statement ====================

createsequencestmt
    : CREATE SEQUENCE IDENTIFIER
    ;

// ==================== CREATE USER Statement ====================

createuserstmt
    : CREATE USER IDENTIFIER (WITH useroptions)*
    ;

// ==================== CREATE DATABASE Statement ====================

createdbstmt
    : CREATE DATABASE IDENTIFIER (WITH dboption)*
    ;

dboption
    : ENCODING EQ IDENTIFIER
    | ENCODING EQ SCONST
    | DBCOMPATIBILITY EQ IDENTIFIER
    ;

useroptions
    : IDENTIFIER
    | IDENTIFIER EQ IDENTIFIER
    ;

// ==================== ANALYZE Statement ====================

analyzestmt
    : ANALYZE (IDENTIFIER)?
    ;

// ==================== TRUNCATE Statement ====================

truncatestmt
    : TRUNCATE (TABLE)? IDENTIFIER
    ;

// ==================== ALTER Statement ====================

alterstmt
    : ALTER TABLE IDENTIFIER altertablecmds
    | alterForeignTableStmt
    ;

altertablecmds
    : altertablecmd (',' altertablecmd)*
    ;

altertablecmd
    : ADD COLUMN columnDef
    | DROP COLUMN IDENTIFIER
    | ADD tableconstraint
    ;

// ==================== Expressions ====================

expr
    : aexpr
    ;

aexpr
    : cexpr
    | aexpr '+' aexpr
    | aexpr '-' aexpr
    | aexpr '*' aexpr
    | aexpr '/' aexpr
    | aexpr '=' aexpr
    | aexpr NOT_EQ aexpr
    | aexpr '<' aexpr
    | aexpr '>' aexpr
    | aexpr LTE_OP aexpr
    | aexpr GTE_OP aexpr
    | aexpr AND aexpr
    | aexpr OR aexpr
    | NOT aexpr
    | aexpr LIKE aexpr
    | aexpr NOT LIKE aexpr
    | aexpr IN inExpr
    | aexpr NOT IN inExpr
    | aexpr IS NULL_P
    | aexpr IS NOT NULL_P
    | aexpr IS TRUE_P
    | aexpr IS NOT TRUE_P
    | aexpr IS FALSE_P
    | aexpr IS NOT FALSE_P
    | aexpr BETWEEN aexpr AND aexpr
    | aexpr NOT BETWEEN aexpr AND aexpr
    | '(' aexpr ')'
    ;

cexpr
    : qualifiedIdentifier
    | ICONST
    | FCONST
    | SCONST
    | TRUE_P
    | FALSE_P
    | NULL_P
    | ON
    | OFF
    | functionCall
    | '(' selectstmt ')'
    | '(' aexpr ')'
    ;

qualifiedIdentifier
    : IDENTIFIER ('.' IDENTIFIER)*
    ;

functionCall
    : IDENTIFIER '(' ')'
    | IDENTIFIER '(' aexpr ')'
    | IDENTIFIER '(' aexpr ',' exprList ')'
    | COUNT '(' '*' ')'
    | COUNT '(' aexpr ')'
    | AVG '(' aexpr ')'
    | SUM '(' aexpr ')'
    | MIN '(' aexpr ')'
    | MAX '(' aexpr ')'
    ;

inExpr
    : '(' selectstmt ')'
    | '(' exprList ')'
    ;

// ==================== Hints (OpenGauss) ====================

hintBlock
    : HINT_COMMENT
    ;

hintItem
    : NESTLOOP LPAREN tableHintList RPAREN
    | MERGEJOIN LPAREN tableHintList RPAREN
    | HASHJOIN LPAREN tableHintList RPAREN
    ;

tableHintList
    : IDENTIFIER (',' IDENTIFIER)*
    |
    ;

// ==================== Foreign Tables (OpenGauss) ====================

createForeignTableStmt
    : CREATE FOREIGN TABLE IDENTIFIER '(' optforeigntableelementlist ')' SERVER IDENTIFIER optcreateforeigntableoptions
    ;

optcreateforeigntableoptions
    : OPTIONS '(' createForeignTableOptionList ')' optServerOptions
    | SERVER OPTIONS '(' createForeignTableOptionList ')' optTableOptions
    |
    ;

optServerOptions
    : SERVER OPTIONS '(' createForeignTableOptionList ')'
    |
    ;

optTableOptions
    : OPTIONS '(' createForeignTableOptionList ')'
    |
    ;

createForeignTableOptionList
    : createForeignTableOption (',' createForeignTableOption)*
    ;

createForeignTableOption
    : SCONST '=' SCONST
    | SCONST SCONST
    ;

// ALTER FOREIGN TABLE statement
alterForeignTableStmt
    : ALTER FOREIGN TABLE IDENTIFIER alterforeigntablecmds
    ;

alterforeigntablecmds
    : alterforeigntablecmd (',' alterforeigntablecmd)*
    ;

alterforeigntablecmd
    : ADD COLUMN columnDef
    | DROP COLUMN IDENTIFIER
    | ALTER COLUMN IDENTIFIER altercolumnaction
    | SERVER IDENTIFIER
    | OPTIONS '(' alterForeignTableOptionList ')'
    | RENAME TO IDENTIFIER
    | SET SCHEMA IDENTIFIER
    ;

altercolumnaction
    : TYPE typename
    | SET TYPE typename
    | SET DEFAULT expr
    | DROP DEFAULT
    ;

alterForeignTableOptionList
    : alterForeignTableOption (',' alterForeignTableOption)*
    ;

alterForeignTableOption
    : SET SCONST '=' SCONST
    | SET SCONST SCONST
    | DROP SCONST
    ;

// Foreign table column definitions with options
optforeigntableelementlist
    : foreigntableelementlist
    |
    ;

foreigntableelementlist
    : foreigntableelement (',' foreigntableelement)*
    ;

foreigntableelement
    : foreigncolumnDef
    | tableconstraint
    ;

foreigncolumnDef
    : IDENTIFIER typename foreigncolquallist?
    ;

foreigncolquallist
    : foreigncolqual*
    ;

foreigncolqual
    : colconstraint
    | OPTIONS '(' foreignColumnOptionList ')'
    ;

foreignColumnOptionList
    : foreignColumnOption (',' foreignColumnOption)*
    ;

foreignColumnOption
    : SCONST '=' SCONST
    | SCONST SCONST
    ;

// ==================== Lexer Rules ====================

// Keywords
IF: I F;
EXISTS: E X I S T S;
CASCADE: C A S C A D E;
RESTRICT: R E S T R I C T;
SELECT: S E L E C T;
FROM: F R O M;
WHERE: W H E R E;
GROUP: G R O U P;
BY: B Y;
HAVING: H A V I N G;
ORDER: O R D E R;
ASC: A S C;
DESC: D E S C;
NULLS: N U L L S;
FIRST: F I R S T;
LAST: L A S T;
LIMIT: L I M I T;
OFFSET: O F F S E T;
INSERT: I N S E R T;
INTO: I N T O;
VALUES: V A L U E S;
DEFAULT: D E F A U L T;
DO: D O;
UPDATE: U P D A T E;
SET: S E T;
DELETE: D E L E T E;
RETURNING: R E T U R N I N G;
CREATE: C R E A T E;
TABLE: T A B L E;
PROCEDURE: P R O C E D U R E;
REPLACE: R E P L A C E;
OUT: O U T;
INOUT: I N O U T;
LANGUAGE: L A N G U A G E;
SECURITY: S E C U R I T Y;
INVOKER: I N V O K E R;
DEFINER: D E F I N E R;
CONSTRAINT: C O N S T R A I N T;
NOT: N O T;
NULL_P: N U L L;
NOTHING: N O T H I N G;
CHECK: C H E C K;
PRIMARY: P R I M A R Y;
KEY: K E Y;
 UNIQUE: U N I Q U E;
 PARTITION: P A R T I T I O N;
 RANGE: R A N G E;
 LIST: L I S T;
HASH: H A S H;
 PARTITIONS: P A R T I T I O N S;
 SUBPARTITION: S U B P A R T I T I O N;
 SUBPARTITIONS: S U B P A R T I T I O N S;
DROP: D R O P;
ALTER: A L T E R;
ADD: A D D;
COLUMN: C O L U M N;
FOREIGN: F O R E I G N;
SERVER: S E R V E R;
OPTIONS: O P T I O N S;
DISTINCT: D I S T I N C T;
ALL: A L L;
AS: A S;
BETWEEN: B E T W E E N;
IN: I N;
LIKE: L I K E;
TRUE_P: T R U E;
FALSE_P: F A L S E;
IS: I S;
AND: A N D;
WITH: W I T H;
RECURSIVE: R E C U R S I V E;
OR: O R;
BEGIN: B E G I N;
COMMIT: C O M M I T;
ROLLBACK: R O L L B A C K;
END: E N D;
SAVEPOINT: S A V E P O I N T;
RELEASE: R E L E A S E;
WORK: W O R K;
NESTLOOP: N E S T L O O P;
MERGEJOIN: M E R G E J O I N;
HASHJOIN: H A S H J O I N;
OWNER: O W N E R;
RENAME: R E N A M E;
TO: T O;
SCHEMA: S C H E M A;
TYPE: T Y P E;
JOIN: J O I N;
INNER: I N N E R;
LEFT: L E F T;
RIGHT: R I G H T;
FULL: F U L L;
OUTER: O U T E R;
CROSS: C R O S S;
ON: O N;
OFF: O F F;
USING: U S I N G;
COUNT: C O U N T;
CONFLICT: C O N F L I C T;
AVG: A V G;
SUM: S U M;
MIN: M I N;
MAX: M A X;
CALL: C A L L;
SHOW: S H O W;
EXPLAIN: E X P L A I N;
ANALYZE: A N A L Y Z E;
THEN: T H E N;
ELSE: E L S E;
ELSIF: E L S I F;
END_IF: E N D IF;
DISTRIBUTE: D I S T R I B U T E;
REPLICATE: R E P L I C A T E;
LESS: L E S S;
THAN: T H A N;
MAXVALUE: M A X V A L U E;
ARROW: '=' '>';
DUPLICATE: D U P L I C A T E;
TRANSACTION: T R A N S A C T I O N;
SCROLL: S C R O L L;
CURSOR: C U R S O R;
UNION: U N I O N;
EXCEPT: E X C E P T;
REFERENCES: R E F E R E N C E;
INDEX: I N D E X;
SERIAL: S E R I A L;
BIGSERIAL: B I G S E R I A L;
SMALLSERIAL: S M A L L S E R I A L;
IDENTITY: I D E N T I T Y;
GENERATED: G E N E R A T E D;
ALWAYS: A L W A Y S;
SEQUENCE: S E Q U E N C E;
CYCLE: C Y C L E;
NO: N O;
NEXT: N E X T;
ROWS: R O W S;
PERCENT: P E R C E N T;
WITHIN: W I T H I N;
GROUPING: G R O U P I N G;
CUBE: C U B E;
ROLLUP: R O L L U P;
FILTER: F I L T E R;
OVER: O V E R;
WINDOW: W I N D O W;
RESPECT: R E S P E C T;
NULLS_ORDER: N U L L S ORDER;
TIES: T I E S;
MEDIAN: M E D I A N;
ORIENTATION: O R I E N T A T I O N;
ROW: R O W;
DEFERRABLE: D E F F E R R A B L E;
ISOLATION: I S O L A T I O N;
LEVEL: L E V E L;
READ: R E A D;
WRITE: W R I T E;
PRIOR: P R I O R;
FORWARD: F O R W A R D;
BACKWARD: B A C K W A R D;
COMMITTED: C O M M I T T E D;
UNCOMMITTED: U N C O M M I T T E D;
SERIALIZABLE: S E R I A L I Z A B L E;
STORAGE: S T O R A G E;
CACHING: C A C H I N G;
DELIMITER: D E L I M I T E R;
HEADER: H E A D E R;
FREEZE: F R E E Z E;
LOGGED: L O G G E D;
UNLOGGED: U N L O G G E D;
INHERIT: I N H E R I T;
INHERITS: I N H E R I T S;
TABLESPACE: T A B L E S P A C E;
BINARY: B I N A R Y;
WITHOUT: W I T H O U T;
TRUNCATE: T R U N C A T E;
TRIGGER: T R I G G E R;
VOLATILE: V O L A T I L E;
STABLE: S T A B L E;
IMMUTABLE: I M M U T A B L E;
STRICT: S T R I C T;
PARALLEL: P A R A L L E L;
SAFE: S A F E;
UNSAFE: U N S A F E;
COST: C O S T;
SUPPORT: S U P P O R T;
BEFORE: B E F O R E;
AFTER: A F T E R;
INSTEAD: I N S T E A D;
EACH: E A C H;
STATEMENT: S T A T E M E N T;
EXECUTE: E X E C U T E;
VERBOSE: V E R B O S E;
COSTS: C O S T S;
BUFFERS: B U F F E R S;
GENERIC_PLAN: G E N E R I C P L A N;
ENCODING: E N C O D I N G;
TEMPORARY: T E M P O R A R Y;
TEMP: T E M P;
LOCAL: L O C A L;
MODIFY: M O D I F Y;
AUTOINCREMENT: A U T O I N C R E M E N T;
INITIALLY: I N I T I A L L Y;
DEFERRED: D E F E R R E D;
START: S T A R T;
COLLATE: C O L L A T E;
STORED: S T O R E D;
USER: U S E R;
DATABASE: D A T A B A S E;
OBJECT: O B J E C T;
DBCOMPATIBILITY: D B C O M P A T I B I L I T Y;
PACKAGE: P A C K A G E;

// Operators
EQ: '=';
COMMA: ',';
NOT_EQ: '!=' | '<>';
LTE_OP: '<=';
GTE_OP: '>=';
PLUS_OP: '+';
MINUS_OP: '-';
MULT_OP: '*';
DIV_OP: '/';

// Parentheses
LPAREN: '(';
RPAREN: ')';

// Semicolon
SEMI: ';';

// Identifiers and Literals
IDENTIFIER
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

QIDENT
    : '"' (~'"' | '""')* '"'
    ;

ICONST
    : [0-9]+
    ;

FCONST
    : [0-9]+ '.' [0-9]* | '.' [0-9]+
    ;

SCONST
    : '\'' (~'\'' | '\'\'')* '\''
    ;

// Comments and Whitespace
LINE_COMMENT
    : '--' ~[\r\n]* -> skip
    ;

// Hint comments - parse as tokens for hint processing
HINT_COMMENT
    : '/*+' .*? '*/'
    ;

// Regular block comments
BLOCK_COMMENT
    : '/*' ~[+] .*? '*/' -> skip
    ;

WS
    : [ \t\r\n]+ -> skip
    ;

// Case-insensitive letter fragments
fragment A: [aA];
fragment B: [bB];
fragment C: [cC];
fragment D: [dD];
fragment E: [eE];
fragment F: [fF];
fragment G: [gG];
fragment H: [hH];
fragment I: [iI];
fragment J: [jJ];
fragment K: [kK];
fragment L: [lL];
fragment M: [mM];
fragment N: [nN];
fragment O: [oO];
fragment P: [pP];
fragment Q: [qQ];
fragment R: [rR];
fragment S: [sS];
fragment T: [tT];
fragment U: [uU];
fragment V: [vV];
fragment W: [wW];
fragment X: [xX];
fragment Y: [yY];
fragment Z: [zZ];
