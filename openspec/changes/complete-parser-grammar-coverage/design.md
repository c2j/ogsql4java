## Context

**当前状态：**
- 解析器已实现基本SQL语法：SELECT/INSERT/UPDATE/DELETE、简单CREATE TABLE、ALTER、DROP、CALL、SET等
- 当前回归测试通过率：57.8%
- ANTLR4语法文件位于：`src/main/java/com/sdchat/ogsql/grammar/OpenGaussSQL.g4`
- 后端参考语法：`src_common_backend_parser/gram.y`（OpenGauss官方Bison语法，约1MB）
- 架构模式：Visitor模式，AST节点 + ANTLR4生成的Parser + ASTBuilder转换

**约束条件：**
- 保持与现有语法规则的兼容性（不能破坏已支持的语法）
- ANTLR4 4.13.1限制：不能直接在parser规则中使用字符串字面量（必须用lexer tokens）
- 需要同步更新：grammar → AST节点 → StatementType枚举 → ASTVisitor接口 → 所有实现类
- 必须保持Java 17兼容性（避免使用Java 21+特性如模式匹配switch）
- 需要完整的回归测试覆盖

**利益相关者：**
- 使用解析器的应用程序开发者
- 测试团队（需要维护回归测试套件）
- 维护团队（长期维护ANTLR4语法规则）

## Goals / Non-Goals

**Goals:**
- 分阶段实现18个新的语法能力（按优先级排序）
- 保持语法规则模块化，便于后续扩展
- 所有新语法规则必须通过回归测试
- 维持现有测试100%通过率
- 建立可重复的语法添加流程

**Non-Goals:**
- 不实现存储过程体内部语句（如PL/SQL控制流）
- 不实现数据库特定的扩展语法（如Oracle兼容语法）
- 不修改现有AST节点结构（保持向后兼容）
- 不实现语义分析（仅语法解析）

## Decisions

### 1. 增量实现策略（优先级排序）

**决策：** 按优先级分6个里程碑实现，而非一次性全部添加

**理由：**
- 降低风险：每个里程碑独立测试，避免大规模变更导致难以调试
- 快速反馈：高优先级语法先可用，用户可尽早受益
- 便于回滚：单个里程碑有问题可独立回滚

**优先级排序：**
1. **M1（核心事务+基础）**: SAVEPOINT, RELEASE, ROLLBACK TO, INSERT ON CONFLICT, RETURNING
2. **M2（索引+序列）**: CREATE/DROP/ALTER INDEX, CREATE/DROP/ALTER SEQUENCE
3. **M3（视图+锁）**: CREATE/DROP/ALTER VIEW, SELECT locking clauses, LATERAL
4. **M4（类型+域）**: CREATE/DROP/ALTER TYPE, CREATE/DROP/ALTER DOMAIN
5. **M5（权限）**: GRANT, REVOKE, ALTER DEFAULT PRIVILEGES
6. **M6（高级特性）**: 窗口函数, COPY, VACUUM, 游标等

### 2. 语法规则组织方式

**决策：** 在ANTLR4 grammar文件中按功能模块组织规则，而非按字母顺序

**理由：**
- 便于维护：相关规则放在一起，易于查找
- 减少冲突：相关规则的优先级更容易协调
- 符合后端parser gram.y的组织方式，便于对比验证

**模块结构：**
```
// Parser Rules - 按功能分组
1. Statement-level rules (stmt, stmtblock, stmtmulti)
2. SELECT-related rules (selectstmt, fromclause, whereclause, etc.)
3. INSERT/UPDATE/DELETE rules
4. CREATE rules (grouped by object type)
5. ALTER rules (grouped by object type)
6. DROP rules (grouped by object type)
7. Transaction rules
8. Utility rules (COPY, VACUUM, etc.)
9. Expression rules (aexpr, cexpr, etc.)
10. Lexer rules (tokens)
```

### 3. 字符串字面量处理策略

**决策：** 所有新的关键字和运算符必须在lexer层定义为tokens，禁止在parser规则中直接使用字符串字面量

**理由：**
- ANTLR4非组合语法限制：parser规则中不能使用字符串字面量
- 统一性：与现有代码风格保持一致
- 可维护性：所有关键字集中管理，便于查找

**示例：**
```antlr
// ❌ 不推荐（会导致编译错误）
savepointStmt: 'SAVEPOINT' IDENTIFIER;

// ✅ 推荐
SAVEPOINT: S A V E P O I N T;
savepointStmt: SAVEPOINT IDENTIFIER;
```

### 4. AST节点类设计

**决策：** 每个新语句类型创建独立的AST节点类，继承SQLStatement接口

**理由：**
- 类型安全：编译时类型检查，避免类型转换错误
- 可扩展性：每个节点类可独立添加特定属性和方法
- 清晰性：通过类名即可知道语句类型

**命名约定：**
- 语句类名：`{Action}{Object}Statement`（如`CreateIndexStatement`, `SavepointStatement`）
- 属性命名：遵循Java驼峰命名，与SQL关键字对应（如`tableName`, `ifNotExists`）

### 5. Visitor模式实现一致性

**决策：** 所有visitor实现必须覆盖所有visit方法，不支持的操作返回null或默认值

**理由：**
- 编译要求：Java接口实现必须覆盖所有抽象方法
- 健壮性：避免运行时UnsupportedOperationException
- 一致性：不同extractor行为一致

**实现模式：**
```java
@Override
public Void visitSavepointStatement(SavepointStatement statement) {
    // 不支持的语句类型，记录日志或静默处理
    return null;
}
```

## Risks / Trade-offs

### 风险1：语法规则冲突导致解析歧义

**风险：** 新添加的规则可能与现有规则产生冲突（如关键字重叠、优先级问题）

**影响：** 高 - 可能导致回归测试失败或解析错误

**缓解措施：**
- 每个里程碑单独测试，确保无冲突后再合并
- 使用ANTLR4的`grun`工具手动测试语法规则
- 维护冲突检测清单：记录已知的关键字冲突和解决方式

### 风险2：大规模变更导致维护困难

**风险：** 添加大量规则后，grammar文件变得庞大难以维护

**影响：** 中 - 长期维护成本增加

**缓解措施：**
- 严格遵循模块化组织规则
- 添加详细的注释说明每个规则的目的
- 定期代码审查确保新增规则符合规范

### 风险3：性能下降

**风险：** 复杂语法（如窗口函数、嵌套子查询）可能降低解析性能

**影响：** 中 - 影响用户体验

**缓解措施：**
- 性能基准测试：对比添加前后的解析速度
- 优化策略：使用ANTLR4的预测模式（SLL vs LL）
- 设置性能阈值：单个语句解析时间<100ms

### 风险4：与后端parser gram.y不同步

**风险：** 随着时间推移，我们的ANTLR4语法可能与后端parser产生差异

**影响：** 低 - 可能导致某些SQL在后端能执行但在解析器解析失败

**缓解措施：**
- 建立定期同步机制：每季度对比gram.y更新
- 维护差异文档：记录有意的差异（如不支持某些扩展语法）
- 回归测试包含后端parser测试用例的子集

## Migration Plan

### 部署策略

由于这是功能扩展而非破坏性变更，采用**增量部署**：

1. **Milestone 1**: 事务控制 + INSERT增强
   - 预期风险低，先部署验证流程
   - 观察1周确认无问题

2. **Milestones 2-3**: 索引/序列/视图
   - 并行开发，串行部署
   - 每个里程碑间隔1周

3. **Milestones 4-6**: 高级特性
   - 根据实际需求优先级调整顺序
   - 复杂语法（如窗口函数）最后实现

### 回滚策略

- **粒度**：按里程碑回滚（通过git revert）
- **触发条件**：回归测试失败率>5%或发现严重解析错误
- **数据安全**：仅回滚代码，不影响已解析的SQL数据

## Open Questions

1. **窗口函数语法复杂度**：是否需要分阶段实现（先OVER子句，后frame specification）？
2. **GRANT/REVOKE粒度**：是否需要支持所有OpenGauss特权类型，还是仅核心子集？
3. **COPY语法**：是否需要支持所有数据源类型（文件/程序/STDIN/STDOUT）？
4. **性能基准**：当前解析性能基线是什么？在哪里记录性能回归测试结果？
5. **语法版本控制**：是否需要支持多个SQL标准版本（SQL92/SQL99/SQL2003）？

