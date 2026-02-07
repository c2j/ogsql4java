## Why

当前 OpenGauss SQL 解析器缺少对 SQL 语句中表关系的可视化能力。开发人员和测试人员难以直观地理解复杂 SQL 查询中涉及的表结构、关联关系和依赖关系。通过创建集成测试用例来解析 SQL 并生成表关系图，可以帮助用户更好地理解 SQL 语句的数据模型，提高调试和分析效率。

## What Changes

- 新增集成测试类 `TableRelationshipGraphTest`，用于测试 SQL 表关系图的生成功能
- 支持解析简单 SQL（单表查询）和复杂 SQL（多表 JOIN、子查询）
- 生成表关系图数据结构，包含表节点、列信息和表间关系
- 提供图的可视化输出能力（如 DOT 格式或 JSON 格式）
- 添加测试用例覆盖常见 SQL 模式：INNER JOIN、LEFT JOIN、子查询、UNION 等

## Capabilities

### New Capabilities
- `sql-table-relationship-graph`: 解析 SQL 语句并提取表关系信息，生成图结构数据
- `graph-visualization-formatter`: 将表关系图转换为可可视化格式（DOT/JSON）
- `integration-test-sql-parsing`: 集成测试框架，验证 SQL 解析和图生成的正确性

### Modified Capabilities
- （无现有规范需要修改）

## Impact

- **测试代码**: 新增 `src/test/java/com/sdchat/ogsql/integration/TableRelationshipGraphTest.java`
- **AST 模块**: 可能需要扩展 `SelectQuery` 和 `DataSource` 类以支持关系提取
- **元数据模块**: 可能需要增强 `MetadataExtractor` 以提取表间关系
- **依赖**: 可能需要添加图处理库（如 JGraphT）用于图结构操作
- **API**: 新增公共 API 用于获取 SQL 的表关系图
