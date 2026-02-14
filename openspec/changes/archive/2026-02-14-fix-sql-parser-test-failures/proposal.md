## Why

The OpenGauss SQL parser is failing 632 test cases due to missing grammar support for common SQL constructs (DROPSCHEMA, IF statements, SHOW, EXPLAIN commands) and has 7 NullPointerExceptions in procedure body parsing. This blocks users from parsing real-world OpenGauss SQL files and reduces the parser's practical utility.

## What Changes

- Add `DROPSCHEMA` support to the grammar (currently causes "no viable alternative at input 'DROPSCHEMA'")
- Add support for `IF` statements in procedural code (currently causes "missing IDENTIFIER at 'if'")
- Add support for `SHOW` command in SQL statements (currently causes "mismatched input 'SHOW'")
- Add support for `EXPLAIN` command (currently causes "mismatched input 'explain'")
- Fix NullPointerException in procedure body parsing when `procedureBody()` returns null
- Add support for other identified syntax patterns causing regressions

## Capabilities

### New Capabilities

- `drop-schema-support`: Add grammar support for DROP SCHEMA statement
- `procedural-if-statement`: Add grammar support for IF statements in stored procedures/functions
- `show-command-support`: Add grammar support for SHOW statements
- `explain-command-support`: Add grammar support for EXPLAIN statements
- `procedure-body-null-safety`: Fix NullPointerException when procedure body is missing

### Modified Capabilities

- (none - these are grammar enhancements to existing parser capabilities)

## Impact

- Primary: `src/main/java/com/sdchat/ogsql/grammar/OpenGaussSQL.g4` - ANTLR4 grammar file
- Related: Parser visitor code in `src/main/java/com/sdchat/ogsql/ast/visitor/`
- Test files remain unchanged per requirements
- No API changes to public parsing interfaces
