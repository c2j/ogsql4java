## ADDED Requirements

### Requirement: Recursive CTE support
The parser SHALL support WITH RECURSIVE syntax:
- WITH RECURSIVE cte_name AS (...)
- Multiple recursive CTEs
- Recursive term with UNION/UNION ALL

#### Scenario: Basic recursive CTE
- **WHEN** parsing "WITH RECURSIVE cnt(x) AS (SELECT 1 UNION ALL SELECT x+1 FROM cnt WHERE x<10) SELECT * FROM cnt"
- **THEN** parser SHALL produce valid recursive CTE structure

#### Scenario: Multiple recursive CTEs
- **WHEN** parsing "WITH RECURSIVE cte1 AS (...), cte2 AS (...) SELECT ..."
- **THEN** parser SHALL handle multiple recursive CTEs

### Requirement: Window Functions support
The parser SHALL support window function syntax:
- OVER (PARTITION BY ... ORDER BY ...)
- Named window definitions (WINDOW w AS (...))
- All window functions (ROW_NUMBER, RANK, DENSE_RANK, etc.)

#### Scenario: Window function with PARTITION BY
- **WHEN** parsing "SELECT ROW_NUMBER() OVER (PARTITION BY a ORDER BY b) FROM t"
- **THEN** parser SHALL handle window specification

#### Scenario: Named window
- **WHEN** parsing "SELECT SUM(x) OVER w FROM t WINDOW w AS (ORDER BY y)"
- **THEN** parser SHALL handle WINDOW clause

### Requirement: FETCH WITH TIES support
The parser SHALL support FETCH FIRST ... WITH TIES:
- FETCH FIRST n ROWS WITH TIES
- FETCH NEXT n ROWS WITH TIES
- PERCENT option

#### Scenario: FETCH WITH TIES
- **WHEN** parsing "SELECT * FROM t ORDER BY a FETCH FIRST 10 ROWS WITH TIES"
- **THEN** parser SHALL recognize WITH TIES clause

#### Scenario: FETCH PERCENT WITH TIES
- **WHEN** parsing "FETCH FIRST 10 PERCENT ROWS WITH TIES"
- **THEN** parser SHALL handle PERCENT option

### Requirement: NULLS FIRST/LAST support
The parser SHALL support NULLS FIRST and NULLS LAST in ORDER BY:
- ORDER BY col NULLS FIRST
- ORDER BY col NULLS LAST

#### Scenario: NULLS FIRST
- **WHEN** parsing "SELECT * FROM t ORDER BY a NULLS FIRST"
- **THEN** parser SHALL handle NULLS FIRST

### Requirement: GROUP BY ROLLUP/CUBE support
The parser SHALL support GROUP BY extensions:
- ROLLUP(col1, col2)
- CUBE(col1, col2)
- GROUPING SETS(...)

#### Scenario: GROUP BY ROLLUP
- **WHEN** parsing "SELECT a, SUM(b) FROM t GROUP BY ROLLUP(a)"
- **THEN** parser SHALL handle ROLLUP

#### Scenario: GROUP BY CUBE
- **WHEN** parsing "GROUP BY CUBE(a, b)"
- **THEN** parser SHALL handle CUBE

### Requirement: DISTINCT ON support
The parser SHALL support DISTINCT ON syntax:
- DISTINCT ON (col1, col2)

#### Scenario: DISTINCT ON
- **WHEN** parsing "SELECT DISTINCT ON (a) a, b FROM t"
- **THEN** parser SHALL handle DISTINCT ON

### Requirement: Subquery in FROM with ORDER BY
The parser SHALL support ORDER BY and LIMIT in subqueries within FROM

#### Scenario: Subquery with ORDER BY in FROM
- **WHEN** parsing "SELECT * FROM (SELECT * FROM t ORDER BY a) sub"
- **THEN** parser SHALL handle subquery ORDER BY
