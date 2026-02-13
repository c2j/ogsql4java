# 非兼容性清单 (Non-Compatibility List)

本文档明确列出 OpenGauss SQL 解析器**明确不支持**的语法特性。这些语法在回归测试中失败是**预期行为**，不需要修复。

## 说明

- **原因分类**:
  - `复杂度`: 实现过于复杂，超出当前项目范围
  - `优先级`: 低优先级，非核心功能
  - `架构`: 与当前架构设计不符
  - `废弃`: 已废弃或不推荐使用的语法

- **状态**:
  - `已确认`: 明确决定不支持
  - `待评估`: 未来版本可能考虑支持

---

## 1. 数据库管理语句 (Database Administration)

| 语法 | 示例 | 原因 | 状态 |
|------|------|------|------|
| CREATE DATABASE | `CREATE DATABASE testdb;` | 架构: 解析器仅关注SQL语句，不涉及数据库生命周期管理 | 已确认 |
| DROP DATABASE | `DROP DATABASE testdb;` | 架构: 同上 | 已确认 |
| ALTER DATABASE | `ALTER DATABASE testdb ...` | 架构: 同上 | 已确认 |
| CREATE USER/ROLE | `CREATE USER admin;` | 优先级: DCL权限管理暂不支持 | 待评估 |
| DROP USER/ROLE | `DROP USER admin;` | 优先级: 同上 | 待评估 |
| GRANT/REVOKE (系统权限) | `GRANT ALL PRIVILEGES TO admin;` | 优先级: 仅支持对象权限 | 待评估 |
| SECURITY LABEL | `SECURITY LABEL ON ...` | 优先级: 高级安全特性 | 待评估 |

---

## 2. PL/SQL 和存储过程体 (PL/SQL & Procedure Body)

| 语法 | 示例 | 原因 | 状态 |
|------|------|------|------|
| CREATE FUNCTION | `CREATE FUNCTION add(a INT) RETURNS INT...` | 架构: 仅支持PROCEDURE，不支持FUNCTION | 已确认 |
| 控制流语句 | `IF ... THEN ... END IF;` | 复杂度: 需要完整的PL/SQL解析器 | 已确认 |
| 循环语句 | `LOOP ... END LOOP;` | 复杂度: 同上 | 已确认 |
| 异常处理 | `EXCEPTION WHEN ... THEN ...` | 复杂度: 同上 | 已确认 |
| 游标声明 | `DECLARE cursor_name CURSOR FOR...` | 复杂度: 需要游标生命周期管理 | 待评估 |
| 游标操作 | `FETCH/MOVE/CLOSE cursor_name` | 复杂度: 同上 | 待评估 |
| RAISE 语句 | `RAISE NOTICE 'message';` | 复杂度: PL/SQL控制流的一部分 | 已确认 |
| 赋值语句 | `variable := expression;` | 复杂度: PL/SQL变量系统 | 已确认 |

---

## 3. 特定扩展和专有语法 (Vendor-Specific Extensions)

| 语法 | 示例 | 原因 | 状态 |
|------|------|------|------|
| MySQL兼容模式 (B_FORMAT) | `CREATE TABLE ... COLLATE latin1_swedish_ci` | 优先级: OpenGauss特定扩展，非标准SQL | 待评估 |
| Oracle兼容语法 | `SELECT * FROM table1, table2 WHERE ... (+)` | 优先级: 非OpenGauss原生语法 | 待评估 |
| 分布式特定语法 | `SELECT * FROM table DISTRIBUTE BY hash(id)` | 架构: 解析器不处理执行计划 | 已确认 |
| 向量计算扩展 | `CREATE TABLE ... WITH (ORIENTATION=COLUMN)` | 优先级: 存储引擎特定选项 | 待评估 |
| 行压缩语法 | `CREATE TABLE ... COMPRESS` | 优先级: 存储特性 | 待评估 |

---

## 4. 高级表函数和特性 (Advanced Table Functions)

| 语法 | 示例 | 原因 | 状态 |
|------|------|------|------|
| GENERATE_SERIES | `SELECT * FROM generate_series(1, 10);` | 复杂度: 需要表函数支持 | 待评估 |
| XMLTABLE | `SELECT * FROM XMLTABLE(...)` | 优先级: XML处理功能 | 待评估 |
| JSON_TABLE | `SELECT * FROM JSON_TABLE(...)` | 优先级: JSON处理功能 | 待评估 |
| 递归CTE高级特性 | `WITH RECURSIVE ... SEARCH DEPTH FIRST ...` | 复杂度: 需要完整的CTE支持 | 待评估 |
| PIVOT/UNPIVOT | `SELECT * FROM table PIVOT ...` | 优先级: Oracle扩展语法 | 待评估 |

---

## 5. 特定实用工具语句 (Utility Commands)

| 语法 | 示例 | 原因 | 状态 |
|------|------|------|------|
| COPY (完整语法) | `COPY table FROM '/path/to/file';` | 复杂度: 需要文件系统交互 | 待评估 |
| VACUUM 详细选项 | `VACUUM FULL ANALYZE table;` | 优先级: 维护命令 | 待评估 |
| CLUSTER | `CLUSTER table USING index;` | 优先级: 维护命令 | 待评估 |
| REINDEX | `REINDEX TABLE table;` | 优先级: 维护命令 | 待评估 |
| LISTEN/NOTIFY | `LISTEN channel_name;` | 架构: 需要事件系统支持 | 已确认 |
| LOAD | `LOAD 'extension.so';` | 架构: 需要动态加载支持 | 已确认 |

---

## 6. 特定数据类型和操作 (Data Types & Operations)

| 语法 | 示例 | 原因 | 状态 |
|------|------|------|------|
| 自定义类型方法 | `CREATE TYPE ... (METHOD ...)` | 复杂度: 对象类型系统 | 待评估 |
| 域约束高级选项 | `CREATE DOMAIN ... CHECK ...` | 优先级: 基础域支持已实现 | 待评估 |
| 数组切片 | `array_column[1:5]` | 复杂度: 需要数组类型完整支持 | 待评估 |
| 范围类型操作 | `SELECT * FROM table WHERE range @> value` | 优先级: 特定类型操作 | 待评估 |
| 几何类型操作 | `SELECT point '(1,2)'` | 优先级: 特定类型操作 | 待评估 |

---

## 7. 已废弃语法 (Deprecated Syntax)

| 语法 | 替代方案 | 废弃原因 |
|------|----------|----------|
| `EXPLAIN ANALYZE` (旧格式) | `EXPLAIN (ANALYZE, VERBOSE)` | 使用标准选项语法 |
| `LIMIT offset, count` | `LIMIT count OFFSET offset` | 标准SQL语法 |
| `CREATE TABLE ... AS SELECT ... WITH [NO] DATA` | 使用标准 `AS SELECT` | 简化语法 |
| Oracle风格的OUTER JOIN `(+)` | 使用标准 `LEFT/RIGHT JOIN` | 标准SQL语法 |

---

## 8. 特定回归测试文件说明

以下测试文件**预期会失败**，它们包含上述不支持的语法:

### 高优先级修复 (Milestone 1-2 范围)
- `upsert_009.sql` - DROP SCHEMA 语法格式问题 ⭐
- `portals.sql` - START TRANSACTION 语法 ⭐
- `dependent_view1.sql` - CREATE VIEW ⭐
- `plan_hint.sql` - ANALYZE 语法 ⭐
- `seqscan_fusion.sql` - EXPLAIN 语法 ⭐

### 低优先级/不支持
- `sqlLLT.sql` - CREATE DATABASE
- `test_unione_*.sql` - CREATE TYPE
- `gtt_function.sql` - CREATE USER
- `row_compression/*.sql` - 行压缩语法
- `recursive_cte*.sql` - 递归CTE高级特性
- `mysql_signal.sql` - MySQL兼容语法
- `col_joinplan.sql` - GENERATE_SERIES
- `tablesample_3.sql` - GENERATE_SERIES

### PL/SQL相关 (明确不支持)
- `sqlcode_cursor.sql` - 游标声明
- `parallel_enable_function.sql` - CREATE FUNCTION
- `out_param_func.sql` - CREATE FUNCTION
- `forall_save_exceptions.sql` - PL/SQL控制流

---

## 9. 未来可能支持 (Future Considerations)

以下语法**当前不支持**，但未来版本可能考虑:

1. **Q2 2025**: CREATE/DROP VIEW, CREATE INDEX, CREATE SEQUENCE
2. **Q3 2025**: 完整的 GRANT/REVOKE, SAVEPOINT/RELEASE
3. **Q4 2025**: CREATE TYPE, CREATE DOMAIN
4. **2026+**: 窗口函数, LATERAL JOIN, 游标支持

---

## 10. 相关文档

- [design.md](./design.md) - 技术设计文档
- [tasks.md](./tasks.md) - 实施任务清单
- [proposal.md](./proposal.md) - 变更提案

---

*最后更新: 2026-02-08*  
*版本: 0.1.0*
