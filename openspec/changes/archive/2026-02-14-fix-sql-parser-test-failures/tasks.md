## 1. Grammar Analysis and Setup

- [x] 1.1 Examine current OpenGaussSQL.g4 grammar structure
- [x] 1.2 Identify where DROP SCHEMA, IF, SHOW, EXPLAIN rules should be added
- [x] 1.3 Run mvn generate-sources to ensure current grammar compiles

## 2. DROP SCHEMA Support

- [x] 2.1 Add DROPSCHEMA keyword to lexer
- [x] 2.2 Add DROP SCHEMA rule to parser
- [x] 2.3 Support IF EXISTS clause
- [x] 2.4 Support CASCADE/RESTRICT options

## 3. SHOW Command Support

- [x] 3.1 Add SHOW keyword to lexer
- [x] 3.2 Add SHOW statement rule to parser

## 4. EXPLAIN Command Support

- [x] 4.1 Add EXPLAIN keyword to lexer
- [x] 4.2 Add EXPLAIN statement rule to parser
- [x] 4.3 Support ANALYZE, VERBOSE, COSTS, BUFFERS, TIMING, FORMAT options

## 5. Procedure Body Null Safety

- [x] 5.1 Identify visitor code that causes NullPointerException
- [x] 5.2 Add null checks for procedureBody() in ASTBuilder.java

## 6. Transaction and Cursor Support

- [x] 6.1 Add START TRANSACTION / BEGIN
- [x] 6.2 Add CURSOR statement
- [x] 6.3 Add FETCH statement

## 7. INSERT/UPDATE Enhancements

- [x] 7.1 Add ON DUPLICATE KEY UPDATE (MySQL/OpenGauss UPSERT)
- [x] 7.2 Add SERIAL, BIGSERIAL, SMALLSERIAL types

## 8. CREATE TABLE Enhancements

- [x] 8.1 Add DISTRIBUTE BY support
- [x] 8.2 Add PARTITION VALUES LESS THAN support
- [x] 8.3 Add WITH clause for table options
- [x] 8.4 Add MAXVALUE for partitions

## 9. Additional Keywords Added

- [x] 9.1 UNION, EXCEPT, INTERSECT
- [x] 9.2 REFERENCES, INDEX, FOREIGN KEY
- [x] 9.3 IDENTITY, GENERATED, ALWAYS
- [x] 9.4 SEQUENCE, CYCLE, NO
- [x] 9.5 WINDOW, OVER, FILTER
- [x] 9.6 READ, WRITE, ISOLATION, LEVEL
- [x] 9.7 SCROLL, CURSOR, FORWARD, BACKWARD
- [x] 9.8 ORIENTATION, COLUMN, ROW

## Progress Summary

- Fixed 7 NullPointerExceptions (Errors: 7 → 0)
- Remaining: 633 syntax failures
- Goal: Support all SQL in src_common_backend_parser/gram.y
