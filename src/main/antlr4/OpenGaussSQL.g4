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

createStatement
    : CREATE TABLE identifier LPAREN columnDefList RPAREN SEMICOLON
    ;

columnDefList
    : columnDef (COMMA columnDef)*
    ;

columnDef
    : identifier (NOT NULL)? (DEFAULT literal)?
    ;

expression
    : literal
    | identifier
    | expression PLUS expression
    | expression MINUS expression
    | expression STAR expression
    | expression SLASH expression
    | LPAREN expression RPAREN
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
