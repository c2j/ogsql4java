## ADDED Requirements

### Requirement: PARTITION BY RANGE support
The parser SHALL support partition by range:
- PARTITION BY RANGE (column)
- PARTITION partition_name VALUES LESS THAN (value)
- MAXVALUE

#### Scenario: CREATE TABLE PARTITION BY RANGE
- **WHEN** parsing "CREATE TABLE t (a INT) PARTITION BY RANGE (a) (PARTITION p1 VALUES LESS THAN (100), PARTITION p2 VALUES LESS THAN (200))"
- **THEN** parser SHALL handle PARTITION BY RANGE

#### Scenario: MAXVALUE partition
- **WHEN** "PARTITION pmax VALUES LESS THAN (MAXVALUE)"
- **THEN** parser SHALL handle MAXVALUE

### Requirement: PARTITION BY LIST support
The parser SHALL support partition by list:
- PARTITION BY LIST (column)
- PARTITION name VALUES IN (value1, value2)

#### Scenario: CREATE TABLE PARTITION BY LIST
- **WHEN** "CREATE TABLE t (a INT) PARTITION BY LIST (a) (PARTITION p1 VALUES IN (1,2), PARTITION p2 VALUES IN (3,4))"
- **THEN** parser SHALL handle PARTITION BY LIST

### Requirement: PARTITION BY HASH support
The parser SHALL support partition by hash:
- PARTITION BY HASH (column)
- PARTITIONS number

#### Scenario: CREATE TABLE PARTITION BY HASH
- **WHEN** "CREATE TABLE t (a INT) PARTITION BY HASH (a) PARTITIONS 4"
- **THEN** parser SHALL handle PARTITION BY HASH

### Requirement: Subpartition support
The parser SHALL support subpartitioning:
- SUBPARTITION BY RANGE/LIST/HASH
- SUBPARTITION templates
- Nested partitions

#### Scenario: Subpartition by range
- **WHEN** "PARTITION BY RANGE (a) SUBPARTITION BY LIST (b) (PARTITION p1 VALUES LESS THAN (100) (SUBPARTITION s1 VALUES IN (1)))"
- **THEN** parser SHALL handle subpartition

### Requirement: ALTER TABLE partition operations
The parser SHALL support ALTER TABLE for partitions:
- ADD PARTITION
- DROP PARTITION
- TRUNCATE PARTITION
- RENAME PARTITION
- MODIFY PARTITION

#### Scenario: ADD PARTITION
- **WHEN** "ALTER TABLE t ADD PARTITION p3 VALUES LESS THAN (300)"
- **THEN** parser SHALL handle ADD PARTITION

#### Scenario: DROP PARTITION
- **WHEN** "ALTER TABLE t DROP PARTITION p1"
- **THEN** parser SHALL handle DROP PARTITION

### Requirement: Partition with specific storage
The parser SHALL support storage parameters on partitions:
- TABLESPACE
- STORAGE
- COMPRESS

#### Scenario: Partition with TABLESPACE
- **WHEN** "PARTITION p1 VALUES LESS THAN (100) TABLESPACE ts1"
- **THEN** parser SHALL handle TABLESPACE clause

### Requirement: Partition column specifications
The parser SHALL support partitioning column options

#### Scenario: INTERVAL partitioning
- **WHEN** "PARTITION BY RANGE (created_date) INTERVAL (INTERVAL '1 month')"
- **THEN** parser SHALL handle INTERVAL in partitioning
