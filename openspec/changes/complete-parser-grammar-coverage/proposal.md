## Why

当前OpenGauss SQL解析器仅支持有限的SQL语法子集（约57.8%通过率）。通过对比`src_common_backend_parser/gram.y`（OpenGauss后端解析器的完整Bison语法文件，约1MB，包含所有SQL语法规则）与当前ANTLR4语法实现，发现大量关键SQL语法尚未支持，包括：事务控制语句（SAVEPOINT/RELEASE）、复杂DDL（CREATE TYPE/DOMAIN/OPERATOR）、DCL（GRANT/REVOKE）、以及高级SELECT特性（WINDOW函数、LATERAL JOIN等）。这限制了解析器在实际生产环境中的可用性，需要系统性地扩展语法覆盖率达到生产级要求。

## What Changes

**新增语法支持（基于后端parser gram.y对比分析）：**

1. **事务控制语句扩展**
   - `SAVEPOINT` / `RELEASE SAVEPOINT` / `ROLLBACK TO SAVEPOINT`
   - `PREPARE TRANSACTION` / `COMMIT PREPARED` / `ROLLBACK PREPARED`
   - `LOCK TABLE` 语句

2. **DDL语句扩展**
   - `CREATE TYPE` / `DROP TYPE` / `ALTER TYPE`
   - `CREATE DOMAIN` / `DROP DOMAIN` / `ALTER DOMAIN`
   - `CREATE SEQUENCE` / `DROP SEQUENCE` / `ALTER SEQUENCE`
   - `CREATE INDEX` / `DROP INDEX` / `ALTER INDEX` / `REINDEX`
   - `CREATE VIEW` / `DROP VIEW` / `ALTER VIEW`
   - `CREATE TRIGGER` / `DROP TRIGGER` / `ALTER TRIGGER`
   - `CREATE RULE` / `DROP RULE`
   - `CREATE OPERATOR` / `DROP OPERATOR`
   - `CREATE CAST` / `DROP CAST`

3. **DCL（数据控制语言）**
   - `GRANT` / `REVOKE` 权限管理
   - `ALTER DEFAULT PRIVILEGES`

4. **SELECT高级特性**
   - `WINDOW` 子句和窗口函数定义
   - `LATERAL` 子查询
   - `TABLESAMPLE` 抽样
   - `FOR UPDATE` / `FOR SHARE` / `FOR KEY SHARE` / `FOR NO KEY UPDATE` 锁子句
   - `OFFSET` 支持（独立子句）

5. **INSERT增强**
   - `INSERT ON CONFLICT`（Upsert语法）
   - `RETURNING` 子句

6. **其他实用语句**
   - `COPY` 数据导入导出
   - `VACUUM` / `ANALYZE` / `CLUSTER`
   - `LISTEN` / `NOTIFY` / `UNLISTEN`
   - `LOAD` / `SHOW` / `RESET` / `DISCARD`
   - `DEALLOCATE` / `EXECUTE`（预备语句）
   - `DECLARE` / `FETCH` / `MOVE` / `CLOSE`（游标）

**实现方式：**
- 在ANTLR4 grammar文件(`OpenGaussSQL.g4`)中添加新规则
- 创建对应的AST节点类
- 在`ASTBuilder`中实现visitor方法
- 更新`StatementType`枚举和`ASTVisitor`接口
- 为所有实现类添加visitor方法
- 编写回归测试验证语法正确性

## Capabilities

### New Capabilities
- `transaction-control-extended`: SAVEPOINT/RELEASE/PREPARE TRANSACTION等事务控制语句
- `ddl-type-domain`: CREATE/DROP/ALTER TYPE和DOMAIN
- `ddl-sequence`: 序列操作语句
- `ddl-index`: 索引操作语句
- `ddl-view`: 视图操作语句
- `ddl-trigger-rule`: 触发器和规则操作
- `ddl-operator-cast`: 自定义操作符和类型转换
- `dcl-grant-revoke`: 权限管理语句
- `select-window-functions`: 窗口函数和WINDOW子句
- `select-lateral-join`: LATERAL子查询支持
- `select-locking-clause`: FOR UPDATE/SHARE锁子句
- `insert-upsert`: INSERT ON CONFLICT语法
- `insert-returning`: RETURNING子句
- `utility-copy`: COPY数据导入导出
- `utility-maintenance`: VACUUM/ANALYZE/CLUSTER维护语句
- `utility-notify`: LISTEN/NOTIFY事件通知
- `utility-cursor`: DECLARE/FETCH/MOVE游标操作
- `utility-prepared-stmt`: DEALLOCATE/EXECUTE预备语句

### Modified Capabilities
- （无现有spec需要修改，这是全新功能扩展）

## Impact

**受影响的代码：**
- `src/main/java/com/sdchat/ogsql/grammar/OpenGaussSQL.g4` - 扩展ANTLR4语法规则
- `src/main/java/com/sdchat/ogsql/ast/` - 新增约20+个AST节点类
- `src/main/java/com/sdchat/ogsql/parser/ASTBuilder.java` - 添加visitor实现
- `src/main/java/com/sdchat/ogsql/ast/StatementType.java` - 扩展枚举
- `src/main/java/com/sdchat/ogsql/visitor/ASTVisitor.java` - 扩展接口方法
- `src/main/java/com/sdchat/ogsql/graph/TableRelationshipExtractor.java` - 实现visitor
- `src/main/java/com/sdchat/ogsql/metadata/MetadataExtractor.java` - 实现visitor
- `src/test/java/com/sdchat/ogsql/contract/` - 新增测试类

**依赖：**
- ANTLR4 4.13.1（已存在）
- Java 17（已存在）
- 参考OpenGauss后端parser gram.y语法规则

**风险：**
- 语法规则冲突：需要仔细处理与现有规则的优先级和冲突
- 回归测试：大量新增规则可能影响现有解析准确性，需要全面测试
- 性能影响：复杂语法（如窗口函数）可能增加解析时间

**目标指标：**
- 将语法覆盖率从57.8%提升至85%+
- 支持OpenGauss核心SQL标准合规语句
- 所有新增语法有对应的回归测试覆盖
