## 1. 项目配置和依赖

- [x] 1.1 在 pom.xml 中添加 JGraphT 依赖
- [x] 1.2 更新 AGENTS.md 记录新的依赖项
- [x] 1.3 运行 `mvn clean compile` 验证依赖正确加载

## 2. 核心数据模型实现

- [x] 2.1 创建 `TableNode` 类（表节点）
- [x] 2.2 创建 `ColumnInfo` 类（列信息）
- [x] 2.3 创建 `RelationshipEdge` 类（关系边）
- [x] 2.4 创建 `JoinCondition` 类（JOIN 条件）
- [x] 2.5 创建 `JoinType` 枚举（INNER_JOIN, LEFT_JOIN, RIGHT_JOIN, FULL_JOIN）
- [x] 2.6 创建 `TableRelationshipGraph` 类（图容器）

## 3. SQL 关系提取器实现

- [x] 3.1 创建 `TableRelationshipExtractor` 类继承 `ASTVisitor`
- [x] 3.2 实现 `visitTableReference` 方法提取表节点
- [x] 3.3 实现 `visitJoinClause` 方法提取 JOIN 关系
- [x] 3.4 实现 `visitColumnReference` 方法提取列信息
- [x] 3.5 实现别名映射表维护逻辑
- [x] 3.6 实现子查询扁平化处理逻辑
- [x] 3.7 添加错误处理和警告日志

## 4. 图格式化器实现

- [x] 4.1 创建 `GraphFormatter<T>` 接口
- [x] 4.2 创建 `DotFormatter` 类实现 DOT 格式输出
- [x] 4.3 创建 `JsonFormatter` 类实现 JSON 格式输出
- [x] 4.4 创建 `FormatterFactory` 工厂类
- [x] 4.5 编写单元测试验证格式化器输出

## 5. 集成测试实现

- [x] 5.1 创建 `TableRelationshipGraphTest` 测试类
- [x] 5.2 编写简单 SQL 测试用例（单表查询）
- [x] 5.3 编写 JOIN 测试用例（INNER JOIN, LEFT JOIN）
- [x] 5.4 编写多表 JOIN 测试用例（3+ 表）
- [x] 5.5 编写子查询测试用例
- [x] 5.6 编写 UNION 测试用例
- [x] 5.7 编写图结构验证测试（节点数、边数、JOIN 条件）
- [x] 5.8 编写 DOT 格式输出验证测试
- [x] 5.9 编写 JSON 格式输出验证测试
- [x] 5.10 运行所有测试并确保通过

## 6. 文档和示例

- [x] 6.1 编写 API 使用文档（README 更新）
- [x] 6.2 创建简单 SQL 示例代码
- [x] 6.3 创建复杂 SQL 示例代码
- [x] 6.4 添加示例输出（DOT 和 JSON）
- [x] 6.5 更新项目主 README 添加功能介绍

## 7. 代码审查和优化

- [x] 7.1 运行 `mvn test` 确保所有测试通过
- [x] 7.2 运行 `mvn clean package` 确保打包成功
- [x] 7.3 检查代码覆盖率报告
- [x] 7.4 进行代码审查（命名规范、注释完整性）
- [x] 7.5 优化性能（如有必要）
