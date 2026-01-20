// OpenGauss SQL Grammar
// Version: 1.0.0
// Generated for ANTLR 4.13.1

grammar OpenGaussSQL;

@header {
package com.sdchat.ogsql.grammar;
}

@members {
}

// Lexer Rules
WS
    : [ \t\r\n]+ -> skip
    ;

// SQL Comments
LINE_COMMENT
    : '--' ~[\r\n]* -> skip
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;

// Parser Rules
prog
    : statement* EOF
    ;

statement
    : selectStatement
    | insertStatement
    | updateStatement
    | deleteStatement
    | createStatement
    | createProcedureStatement
    ;

selectStatement
    : SELECT (DISTINCT)? selectList FROM identifier (WHERE expression)? (GROUP BY expression)? (HAVING expression)? (ORDER BY orderBy)? (LIMIT expression)? SEMICOLON
    ;

selectList
    : selectItem (COMMA selectItem)*
    ;

selectItem
    : expression (AS identifier)?
    ;

insertStatement
    : INSERT INTO identifier (LPAREN identifierList RPAREN)? VALUES valueList SEMICOLON
    ;

identifierList
    : identifier (COMMA identifier)*
    ;

valueList
    : LPAREN valueExpression (COMMA valueExpression)* RPAREN
    ;

updateStatement
    : UPDATE identifier SET setClause (WHERE expression)? SEMICOLON
    ;

setClause
    : identifier EQ expression (COMMA identifier EQ expression)*
    ;

deleteStatement
    : DELETE FROM identifier (WHERE expression)? SEMICOLON
    ;

createProcedureStatement
    : CREATE (OR REPLACE)? PROCEDURE identifier LPAREN parameterList? RPAREN (LANGUAGE identifier)? (SECURITY DEFINER)? AS dollarString SEMICOLON?
    ;

dollarString
    : '$$' (~'$'? | '$' ~'$')* '$$'
    ;

parameterList
    : parameterDef (COMMA parameterDef)*
    ;

parameterDef
    : identifier (IN | OUT | INOUT)? dataType (DEFAULT literal)?
    ;

dataType
    : identifier
    | identifier LPAREN NUMBER RPAREN
    | identifier LPAREN NUMBER COMMA NUMBER RPAREN
    ;

createStatement
    : CREATE TABLE identifier LPAREN columnDefList RPAREN SEMICOLON
    | createProcedureStatement
    ;

columnDefList
    : columnDef (COMMA columnDef)*
    ;

columnDef
    : identifier (NOT NULL)? (DEFAULT literal)?
    ;

expr
    : logicalOrExpr
    ;

logicalOrExpr
    : logicalOrExpr OR logicalAndExpr
    | logicalAndExpr
    ;

logicalAndExpr
    : logicalAndExpr AND notExpr
    | notExpr
    ;

notExpr
    : NOT notExpr
    | comparisonExpr
    ;

comparisonExpr
    : comparisonExpr (EQ | LT | GT | LE | GE | NE) additiveExpr
    | additiveExpr
    ;

additiveExpr
    : additiveExpr (PLUS | MINUS) multiplicativeExpr
    | multiplicativeExpr
    ;

multiplicativeExpr
    : multiplicativeExpr (STAR | SLASH) primaryExpr
    | primaryExpr
    ;

primaryExpr
    : literal
    | identifier
    | LPAREN expr RPAREN
    ;

expression
    : expr
    ;

valueExpression
    : literal
    | identifier
    ;

literal
    : STRING
    | NUMBER
    ;

identifier
    : IDENTIFIER
    ;

orderBy
    : expression (ASC | DESC)?
    ;

// Lexer Rules
SELECT: 'SELECT';
FROM: 'FROM';
WHERE: 'WHERE';
AND: 'AND';
OR: 'OR';
NOT: 'NOT';
INSERT: 'INSERT';
INTO: 'INTO';
VALUES: 'VALUES';
UPDATE: 'UPDATE';
SET: 'SET';
DELETE: 'DELETE';
CREATE: 'CREATE';
TABLE: 'TABLE';
PROCEDURE: 'PROCEDURE';
REPLACE: 'REPLACE';
IN: 'IN';
OUT: 'OUT';
INOUT: 'INOUT';
LANGUAGE: 'LANGUAGE';
SECURITY: 'SECURITY';
DEFINER: 'DEFINER';
DISTINCT: 'DISTINCT';
GROUP: 'GROUP';
BY: 'BY';
HAVING: 'HAVING';
ORDER: 'ORDER';
LIMIT: 'LIMIT';
DEFAULT: 'DEFAULT';
AS: 'AS';
ASC: 'ASC';
DESC: 'DESC';
NULL: 'NULL';

EQ: '=';
LT: '<';
GT: '>';
LE: '<=';
GE: '>=';
NE: '<>' | '!=';
PLUS: '+';
MINUS: '-';
STAR: '*';
SLASH: '/';
COMMA: ',';
SEMICOLON: ';';
LPAREN: '(';
RPAREN: ')';

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
NUMBER: [0-9]+ ('.' [0-9]+)?;
STRING: '\'' (~['\r\n] | '\'\'')* '\'';
