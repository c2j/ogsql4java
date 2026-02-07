## Context

当前 OpenGauss SQL 解析器已具备基本的 SQL 解析能力，可以生成 AST（抽象语法树）。但是，缺少从 AST 中提取表关系信息并可视化的能力。本设计文档描述如何实现 SQL 表关系图的提取和可视化功能，包括核心数据结构、算法和架构决策。

现有的 AST 结构包含 `SelectQuery`、`DataSource`、`Column` 等类，可以在此基础上扩展表关系提取功能。

## Goals / Non-Goals

**Goals:**
- 设计可扩展的表关系图数据结构，支持表节点、列信息和关系边
- 实现从 SQL AST 中提取表关系的算法
- 支持多种输出格式（DOT、JSON）用于可视化
- 创建集成测试框架验证功能正确性
- 保持与现有 AST 和解析器的兼容性

**Non-Goals:**
- 不实现图形界面的可视化渲染（仅生成数据格式）
- 不处理 DML/DDL 语句（仅关注 SELECT 查询）
- 不实现实时 SQL 监控或性能分析
- 不依赖外部数据库连接（纯解析器功能）

## Decisions

### 1. 使用 JGraphT 作为图数据结构库
**Decision**: 引入 JGraphT 库作为底层图数据结构支持。

**Rationale**:
- JGraphT 是成熟的 Java 图库，提供丰富的图算法和数据结构
- 支持有向图、无向图、带权图等多种图类型
- 提供良好的遍历和查询 API
- 与 Java 生态兼容性好

**Alternatives Considered**:
- 自定义图实现：灵活但工作量大，需要自行实现遍历算法
- Apache Commons Graph：功能较简单，缺少高级特性

### 2. 采用 Visitor 模式遍历 AST
**Decision**: 使用 Visitor 模式遍历 ANTLR 生成的 AST 来提取表关系。

**Rationale**:
- 符合现有代码的 ASTVisitor 设计模式
- 将表关系提取逻辑与 AST 结构解耦
- 便于扩展支持新的 SQL 语法

**Implementation**:
- 创建 `TableRelationshipExtractor` 类继承 `ASTVisitor`
- 在访问 `TableReference`、`JoinClause` 等节点时构建图结构

### 3. 图数据模型设计
**Decision**: 采用节点-边模型，节点表示表，边表示 JOIN 关系。

**Node Structure**:
```java
class TableNode {
    String name;           // 表名
    String alias;          // 别名（可选）
    List<ColumnInfo> columns;  // 列信息列表
}

class ColumnInfo {
    String name;           // 列名
    String qualifiedName;  // 限定名（如 users.id）
}
```

**Edge Structure**:
```java
class RelationshipEdge {
    TableNode source;      // 源表
    TableNode target;      // 目标表
    JoinType type;         // INNER_JOIN, LEFT_JOIN, etc.
    List<JoinCondition> conditions;  // JOIN 条件列表
}

class JoinCondition {
    String leftColumn;     // 左表列
    String rightColumn;    // 右表列
}
```

**Rationale**:
- 清晰分离表信息和关系信息
- 支持多条件 JOIN（复合主键场景）
- 易于转换为不同输出格式

### 4. 输出格式采用 Strategy 模式
**Decision**: 使用 Strategy 模式实现多种输出格式转换器。

**Rationale**:
- 符合开闭原则，便于添加新格式
- 统一接口 `GraphFormatter<T>`
- 现有格式：DotFormatter、JsonFormatter

**Interface Design**:
```java
interface GraphFormatter<T> {
    T format(TableRelationshipGraph graph);
}
```

### 5. 子查询扁平化处理
**Decision**: 子查询中的表直接提升到主查询图中，不保留嵌套结构。

**Rationale**:
- 简化图结构，关注物理表关系
- 符合大多数可视化需求
- 避免循环引用问题

**Trade-off**: 丢失了子查询的逻辑边界信息，但对于表关系可视化场景可接受。

## Risks / Trade-offs

**[Risk] 复杂 SQL 解析失败** → **Mitigation**: 提供优雅降级，对于无法解析的部分记录警告但不中断处理

**[Risk] 性能问题（大 SQL）** → **Mitigation**: 设置最大节点/边数量限制，避免内存溢出

**[Risk] 别名解析歧义** → **Mitigation**: 维护别名到实际表的映射表，在列解析时进行查找

**[Trade-off] 子查询扁平化** 丢失了子查询边界，但获得了更清晰的物理表关系视图

**[Trade-off] 仅支持 SELECT** 不支持 INSERT/UPDATE/DELETE 的关系提取，但满足了主要使用场景

## Migration Plan

**Phase 1**: 核心功能实现
1. 添加 JGraphT 依赖到 pom.xml
2. 实现 TableRelationshipGraph 数据模型
3. 实现 TableRelationshipExtractor Visitor

**Phase 2**: 格式化器实现
1. 实现 DotFormatter
2. 实现 JsonFormatter
3. 添加格式化器工厂类

**Phase 3**: 集成测试
1. 创建 TableRelationshipGraphTest 类
2. 编写测试用例覆盖所有 SQL 模式
3. 验证输出格式正确性

**Rollback Strategy**: 功能完全新增，不影响现有代码。如需要回滚，只需删除新增文件和依赖。

## Open Questions

1. **性能基准**: 需要测试多大规模的 SQL 会影响性能？
2. **扩展语法**: 是否需要支持 CTE (WITH 子句)？
3. **可视化工具**: 推荐使用什么工具渲染 DOT 格式？（Graphviz、Web 工具等）
