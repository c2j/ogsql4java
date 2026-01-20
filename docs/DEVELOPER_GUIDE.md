# ogsql 开发者指南

> 面向开发者的 OpenGauss SQL Parser 使用文档
>
> 从简单到复杂的 SQL 解析指南

---

## 目录

1. [快速开始](#快速开始)
2. [简单 SQL 解析](#简单-sql-解析)
3. [复杂 SQL 解析](#复杂-sql-解析)
4. [iBatis XML SQL 解析](#ibatis-xml-sql-解析)
5. [存储过程解析](#存储过程解析)
6. [元数据提取](#元数据提取)
7. [错误处理](#错误处理)
8. [性能优化](#性能优化)
9. [最佳实践](#最佳实践)

---

## 快速开始

### Maven 依赖

```xml
<dependency>
    <groupId>com.sdchat</groupId>
    <artifactId>ogsql</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 基础示例

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SQLStatement;

// 创建解析器实例
SQLParser parser = new SQLParser();

// 解析 SQL 语句
String sql = "SELECT id, name FROM users WHERE active = true";
SQLStatement statement = parser.parse(sql);

// 检查语句类型
System.out.println("语句类型: " + statement.getStatementType());
```

---

## 简单 SQL 解析

### 1. SELECT 语句

#### 基础查询

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.ast.SelectQuery;

SQLParser parser = new SQLParser();
String sql = "SELECT id, name, email FROM users WHERE active = true";
SQLStatement statement = parser.parse(sql);

if (statement instanceof SelectQuery) {
    SelectQuery select = (SelectQuery) statement;
    System.out.println("表名: " + select.getFromClause());
    System.out.println("SELECT 语句类型: " + select.getStatementType());
}
```

#### 带分组和排序的查询

```java
String sql = "SELECT department, COUNT(*) as count " +
             "FROM employees " +
             "WHERE hire_date > '2020-01-01' " +
             "GROUP BY department " +
             "HAVING COUNT(*) > 5 " +
             "ORDER BY count DESC " +
             "LIMIT 10";

SQLStatement statement = parser.parse(sql);
SelectQuery select = (SelectQuery) statement;
// 获取查询结构信息
```

### 2. INSERT 语句

#### 基础插入

```java
import com.sdchat.ogsql.ast.InsertStatement;

String sql = "INSERT INTO users (name, email, active) VALUES ('John', 'john@example.com', true)";
SQLStatement statement = parser.parse(sql);

if (statement instanceof InsertStatement) {
    InsertStatement insert = (InsertStatement) statement;
    System.out.println("插入表: " + insert.getTableName());
}
```

#### 批量插入

```java
String sql = "INSERT INTO orders (user_id, product_id, quantity) " +
             "VALUES (1, 101, 5), (1, 102, 3), (2, 101, 10)";

SQLStatement statement = parser.parse(sql);
InsertStatement insert = (InsertStatement) statement;
```

### 3. UPDATE 语句

```java
import com.sdchat.ogsql.ast.UpdateStatement;

String sql = "UPDATE users SET active = false, last_login = CURRENT_TIMESTAMP " +
             "WHERE last_login < '2020-01-01'";

SQLStatement statement = parser.parse(sql);
UpdateStatement update = (UpdateStatement) statement;
System.out.println("更新表: " + update.getTableName());
```

### 4. DELETE 语句

```java
import com.sdchat.ogsql.ast.DeleteStatement;

String sql = "DELETE FROM logs WHERE created_at < '2020-01-01' LIMIT 1000";

SQLStatement statement = parser.parse(sql);
DeleteStatement delete = (DeleteStatement) statement;
System.out.println("删除表: " + delete.getTableName());
```

---

## 复杂 SQL 解析

### 1. 多表 JOIN 查询

```java
String sql = """
    SELECT u.name, o.order_id, p.product_name, o.amount
    FROM users u
    INNER JOIN orders o ON u.id = o.user_id
    LEFT JOIN products p ON o.product_id = p.id
    WHERE u.active = true AND o.status = 'completed'
    ORDER BY o.amount DESC
    LIMIT 100
    """;

SQLStatement statement = parser.parse(sql);
SelectQuery select = (SelectQuery) statement;
```

### 2. 子查询

```java
String sql = """
    SELECT name, salary
    FROM employees
    WHERE salary > (
        SELECT AVG(salary)
        FROM employees
        WHERE department = 'IT'
    )
    """;

SQLStatement statement = parser.parse(sql);
```

### 3. 性能优化提示 (Hints)

```java
import com.sdchat.ogsql.ast.PerformanceHint;

String sql = """
    /*+ NestLoop(u o) HashJoin(o p) */
    SELECT u.name, o.order_id, p.product_name
    FROM users u
    JOIN orders o ON u.id = o.user_id
    JOIN products p ON o.product_id = p.id
    """;

SQLStatement statement = parser.parse(sql);
SelectQuery select = (SelectQuery) statement;
for (PerformanceHint hint : select.getHints()) {
    System.out.println("提示类型: " + hint.getHintType());
    System.out.println("表引用: " + hint.getTableReferences());
}
```

### 4. CASE 表达式

```java
String sql = """
    SELECT name,
           CASE
               WHEN age < 18 THEN 'Minor'
               WHEN age < 65 THEN 'Adult'
               ELSE 'Senior'
           END as age_group
    FROM users
    """;

SQLStatement statement = parser.parse(sql);
```

### 5. 聚合函数和窗口函数

```java
String sql = """
    SELECT
        department,
        employee_id,
        salary,
        SUM(salary) OVER (PARTITION BY department) as dept_total,
        AVG(salary) OVER () as company_avg,
        ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) as dept_rank
    FROM employees
    ORDER BY department, salary DESC
    """;

SQLStatement statement = parser.parse(sql);
```

### 4. INSERT ... SELECT 语句（数据迁移）

```java
String sql = """
    INSERT INTO archived_orders (
        order_id,
        customer_id,
        order_date,
        total_amount,
        status
    )
    SELECT
        o.id,
        o.customer_id,
        o.order_date,
        SUM(oi.quantity * p.price) as total_amount,
        'ARCHIVED' as status
    FROM orders o
    INNER JOIN order_items oi ON o.id = oi.order_id
    INNER JOIN products p ON oi.product_id = p.id
    WHERE o.created_at < '2023-01-01'
      AND o.status = 'COMPLETED'
    GROUP BY o.id, o.customer_id, o.order_date
    HAVING SUM(oi.quantity * p.price) > 0
    LIMIT 10000
    """;

SQLStatement statement = parser.parse(sql);
InsertStatement insert = (InsertStatement) statement;
System.out.println("目标表: " + insert.getTableName());
```

### 5. 表依赖关系分析

```java
import com.sdchat.ogsql.metadata.MetadataExtractor;

/**
 * 分析 SQL 语句中的表依赖关系
 */
public class DependencyAnalyzer {

    public static void analyzeDependencies(String sql) {
        SQLParser parser = new SQLParser();
        MetadataExtractor extractor = new MetadataExtractor();

        // 1. 解析 INSERT 语句
        SQLStatement statement = parser.parse(sql);

        if (statement instanceof InsertStatement) {
            InsertStatement insert = (InsertStatement) statement;
            String targetTable = insert.getTableName();
            System.out.println("目标表: " + targetTable);
        }

        // 2. 提取所有引用的表
        extractor.extract(statement);
        Set<String> referencedTables = extractor.getTables();

        System.out.println("引用的表: " + referencedTables);

        // 3. 分析依赖关系
        if (statement instanceof InsertStatement) {
            String targetTable = ((InsertStatement) statement).getTableName();
            System.out.println("\n=== 表依赖关系 ===");
            System.out.println("写入表: " + targetTable);
            System.out.println("读取表: " + referencedTables);

            // 检查是否依赖其他表
            if (referencedTables.size() > 1) {
                Set<String> sourceTables = new HashSet<>(referencedTables);
                sourceTables.remove(targetTable);
                if (!sourceTables.isEmpty()) {
                    System.out.println("类型: 直接数据复制");
                    System.out.println("数据流向: " +
                                 String.join(" -> ", sourceTables) + " -> " + targetTable);
                } else {
                    System.out.println("类型: 表连接查询");
                    System.out.println("参与表: " + referencedTables);
                }
            }
        }
    }

    /**
     * 分析多个 SQL 语句的依赖图
     */
    public static void analyzeDependencyGraph(String multiSql) {
        SQLParser parser = new SQLParser();
        MultiParseResult result = parser.parseMultiple(multiSql);

        // 构建依赖图
        Map<String, Set<String>> dependencyMap = new HashMap<>();

        for (SQLStatement stmt : result.getStatements()) {
            MetadataExtractor extractor = new MetadataExtractor();
            extractor.extract(stmt);

            if (stmt instanceof InsertStatement) {
                InsertStatement insert = (InsertStatement) stmt;
                String target = insert.getTableName();
                Set<String> sources = new HashSet<>(extractor.getTables());
                sources.remove(target);
                dependencyMap.put(target, sources);
            }
        }

        // 打印依赖图
        System.out.println("\n=== SQL 依赖图 ===");
        for (Map.Entry<String, Set<String>> entry : dependencyMap.entrySet()) {
            System.out.println(entry.getKey() + " 依赖: " + entry.getValue());
        }

        // 检测循环依赖
        detectCyclicDependencies(dependencyMap);
    }

    private static void detectCyclicDependencies(Map<String, Set<String>> graph) {
        // 简单的循环依赖检测
        Set<String> visited = new HashSet<>();
        for (String table : graph.keySet()) {
            if (hasPathTo(table, table, graph, visited)) {
                System.out.println("警告: 检测到可能的循环依赖，涉及表: " + table);
            }
        }
    }

    private static boolean hasPathTo(String from, String to, Map<String, Set<String>> graph, Set<String> visited) {
        if (from.equals(to)) return true;
        if (visited.contains(from)) return false;
        visited.add(from);

        Set<String> dependencies = graph.get(from);
        if (dependencies == null || dependencies.isEmpty()) return false;

        for (String dep : dependencies) {
            if (hasPathTo(dep, to, graph, new HashSet<>(visited))) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String sql = """
            INSERT INTO log_table (id, log_time, message)
            SELECT id, created_at, 'PROCESSED'
            FROM staging_table
            WHERE status = 'PENDING';

            INSERT INTO aggregated_sales (product_id, total_sales, year)
            SELECT product_id, SUM(amount), EXTRACT(YEAR FROM sale_date)
            FROM sales
            WHERE sale_date >= '2024-01-01'
            GROUP BY product_id, EXTRACT(YEAR FROM sale_date);
            """;

        analyzeDependencyGraph(sql);
    }
}
```

---

## iBatis XML SQL 解析

### 1. 从 XML 中提取 SQL

```java
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

// 示例 iBatis XML
String xmlContent = """
    <mapper namespace="UserMapper">
        <select id="findActiveUsers" resultType="User">
            SELECT id, name, email
            FROM users
            WHERE active = #{active}
            ORDER BY created_at DESC
        </select>

        <insert id="insertUser" parameterType="User">
            INSERT INTO users (name, email, active)
            VALUES (#{name}, #{email}, #{active})
        </insert>

        <update id="updateUserStatus">
            UPDATE users
            SET active = #{active},
                updated_at = CURRENT_TIMESTAMP
            WHERE id = #{id}
        </update>

        <delete id="deleteInactiveUsers">
            DELETE FROM users
            WHERE active = false
              AND created_at < #{cutoffDate}
        </delete>
    </mapper>
    """;

// 解析 XML 提取 SQL
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));

// 提取所有 SQL 语句
SQLParser parser = new SQLParser();

// 处理 <select> 标签
NodeList selects = doc.getElementsByTagName("select");
for (int i = 0; i < selects.getLength(); i++) {
    String sql = selects.item(i).getTextContent().trim();
    String statementId = ((Element) selects.item(i)).getAttribute("id");
    String resultType = ((Element) selects.item(i)).getAttribute("resultType");

    System.out.println("=== SELECT: " + statementId + " ===");
    SQLStatement statement = parser.parse(sql);
    System.out.println("返回类型: " + resultType);
    System.out.println("SQL类型: " + statement.getStatementType());
}

// 处理 <insert> 标签
NodeList inserts = doc.getElementsByTagName("insert");
for (int i = 0; i < inserts.getLength(); i++) {
    String sql = inserts.item(i).getTextContent().trim();
    String statementId = ((Element) inserts.item(i)).getAttribute("id");
    String parameterType = ((Element) inserts.item(i)).getAttribute("parameterType");

    System.out.println("=== INSERT: " + statementId + " ===");
    SQLStatement statement = parser.parse(sql);
    System.out.println("参数类型: " + parameterType);
}

// 处理 <update> 标签
NodeList updates = doc.getElementsByTagName("update");
for (int i = 0; i < updates.getLength(); i++) {
    String sql = updates.item(i).getTextContent().trim();
    String statementId = ((Element) updates.item(i)).getAttribute("id");

    System.out.println("=== UPDATE: " + statementId + " ===");
    SQLStatement statement = parser.parse(sql);
}

// 处理 <delete> 标签
NodeList deletes = doc.getElementsByTagName("delete");
for (int i = 0; i < deletes.getLength(); i++) {
    String sql = deletes.item(i).getTextContent().trim();
    String statementId = ((Element) deletes.item(i)).getAttribute("id");

    System.out.println("=== DELETE: " + statementId + " ===");
    SQLStatement statement = parser.parse(sql);
}
```

### 2. 带动态 SQL 的 XML 解析

```java
String xmlContent = """
    <mapper namespace="OrderMapper">
        <select id="findOrdersByStatus" resultType="Order">
            SELECT o.id, u.name as user_name, o.total_amount
            FROM orders o
            LEFT JOIN users u ON o.user_id = u.id
            WHERE 1=1
            <if test="status != null">
                AND o.status = #{status}
            </if>
            <if test="startDate != null">
                AND o.created_at <![CDATA[ >= ]]> #{startDate}
            </if>
            <if test="endDate != null">
                AND o.created_at <![CDATA[ <= ]]> #{endDate}
            </if>
            ORDER BY o.created_at DESC
        </select>
    </mapper>
    """;

// 提取 SQL (去除 iBatis 标签)
Document doc = parseXml(xmlContent);
NodeList selects = doc.getElementsByTagName("select");

for (int i = 0; i < selects.getLength(); i++) {
    String sql = selects.item(i).getTextContent().trim();

    // 注意：动态 SQL 标签可能需要额外处理
    // 这里演示解析基础 SQL 结构
    try {
        SQLStatement statement = parser.parse(sql);
        System.out.println("成功解析: " + statement.getStatementType());

        // 提取元数据
        MetadataExtractor extractor = new MetadataExtractor();
        extractor.extract(statement);
        System.out.println("引用的表: " + extractor.getTables());
    } catch (Exception e) {
        // 动态 SQL 可能在语法上不完整
        System.out.println("包含动态 SQL，跳过完整解析");
    }
}
```

### 3. 批量解析 XML 中的所有 SQL

```java
import com.sdchat.ogsql.parser.MultiParseResult;

public class IbatisSqlParser {

    public static void parseMapperFile(String xmlContent) {
        SQLParser parser = new SQLParser();
        Document doc = parseXml(xmlContent);

        // 收集所有 SQL
        List<String> sqlStatements = new ArrayList<>();

        // 提取所有标签的 SQL
        String[] tags = {"select", "insert", "update", "delete"};
        for (String tag : tags) {
            NodeList nodes = doc.getElementsByTagName(tag);
            for (int i = 0; i < nodes.getLength(); i++) {
                String sql = nodes.item(i).getTextContent().trim();
                if (!sql.isEmpty()) {
                    sqlStatements.add(sql);
                }
            }
        }

        // 批量解析
        String combinedSql = String.join(";", sqlStatements);
        MultiParseResult result = parser.parseMultiple(combinedSql);

        // 输出结果
        System.out.println("成功解析: " + result.getStatements().size() + " 条语句");
        System.out.println("错误: " + result.getErrors().size());

        for (SQLStatement stmt : result.getStatements()) {
            System.out.println("- " + stmt.getStatementType());
        }

        for (ParseException error : result.getErrors()) {
            System.out.println("错误: " + error.getMessage());
        }
    }

    private static Document parseXml(String xml) {
        // XML 解析实现
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes()));
    }
}
```

---

## 存储过程解析

### 1. CREATE PROCEDURE (入门)

#### 最简单的存储过程

```java
import com.sdchat.ogsql.ast.CreateProcedureStmt;

String sql = "CREATE PROCEDURE hello_world() AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";
SQLStatement statement = parser.parse(sql);

if (statement instanceof CreateProcedureStmt) {
    CreateProcedureStmt proc = (CreateProcedureStmt) statement;
    System.out.println("存储过程名: " + proc.getProcedureName());
    System.out.println("语言: " + proc.getLanguage());
}
```

#### 带参数的存储过程

```java
String sql = """
    CREATE PROCEDURE add_user(
        p_name IN VARCHAR,
        p_email IN VARCHAR,
        p_active IN BOOLEAN DEFAULT true
    )
    LANGUAGE plpgsql
    AS $$
    BEGIN
        INSERT INTO users (name, email, active, created_at)
        VALUES (p_name, p_email, p_active, CURRENT_TIMESTAMP);
    END;
    $$;
    """;

SQLStatement statement = parser.parse(sql);
CreateProcedureStmt proc = (CreateProcedureStmt) statement;

System.out.println("参数数量: " + proc.getParameters().size());
for (com.sdchat.ogsql.ast.ProcedureParameter param : proc.getParameters()) {
    System.out.println("  - " + param.getName() + " " +
                     param.getMode() + " " +
                     param.getDataType());
}
```

### 2. CREATE PROCEDURE (进阶)

#### 带 OUT/INOUT 参数的存储过程

```java
String sql = """
    CREATE PROCEDURE calculate_bonus(
        p_employee_id IN INTEGER,
        p_bonus_rate IN NUMERIC,
        p_total_bonus OUT NUMERIC,
        p_status OUT VARCHAR
    )
    LANGUAGE plpgsql
    AS $$
    DECLARE
        v_salary NUMERIC;
    BEGIN
        SELECT salary INTO v_salary
        FROM employees
        WHERE id = p_employee_id;

        p_total_bonus := v_salary * p_bonus_rate;
        p_status := 'CALCULATED';
    END;
    $$;
    """;

SQLStatement statement = parser.parse(sql);
CreateProcedureStmt proc = (CreateProcedureStmt) statement;

// 分析参数模式
for (com.sdchat.ogsql.ast.ProcedureParameter param : proc.getParameters()) {
    switch (param.getMode()) {
        case IN:
            System.out.println("输入参数: " + param.getName());
            break;
        case OUT:
            System.out.println("输出参数: " + param.getName());
            break;
        case INOUT:
            System.out.println("输入输出参数: " + param.getName());
            break;
    }
}
```

#### 带 SECURITY DEFINER 的存储过程

```java
String sql = """
    CREATE PROCEDURE admin_delete_user(p_user_id IN INTEGER)
    LANGUAGE plpgsql
    SECURITY DEFINER
    AS $$
    BEGIN
        DELETE FROM users WHERE id = p_user_id;
        DELETE FROM user_sessions WHERE user_id = p_user_id;
    END;
    $$;
    """;

SQLStatement statement = parser.parse(sql);
CreateProcedureStmt proc = (CreateProcedureStmt) statement;

if (proc.getSecurity() != null && proc.getSecurity().isDefiner()) {
    System.out.println("以 DEFINER 权限执行");
}
```

#### 带 OR REPLACE 的存储过程

```java
String sql = """
    CREATE OR REPLACE PROCEDURE process_order(
        p_order_id IN INTEGER,
        p_status IN VARCHAR
    )
    LANGUAGE plpgsql
    AS $$
    BEGIN
        UPDATE orders
        SET status = p_status,
            processed_at = CURRENT_TIMESTAMP
        WHERE id = p_order_id;

        INSERT INTO order_logs (order_id, status, processed_at)
        VALUES (p_order_id, p_status, CURRENT_TIMESTAMP);
    END;
    $$;
    """;

SQLStatement statement = parser.parse(sql);
CreateProcedureStmt proc = (CreateProcedureStmt) statement;

if (proc.isOrReplace()) {
    System.out.println("覆盖已存在的存储过程");
}
```

### 3. 存储过程体解析

#### 带变量声明的存储过程

```java
import com.sdchat.ogsql.ast.ProcedureBody;
import com.sdchat.ogsql.ast.VariableDeclaration;

String sql = """
    CREATE PROCEDURE analyze_sales(p_year IN INTEGER)
    LANGUAGE plpgsql
    AS $$
    DECLARE
        v_total_sales NUMERIC;
        v_top_product VARCHAR(100);
        v_customer_count INTEGER;
    BEGIN
        SELECT SUM(amount) INTO v_total_sales
        FROM sales
        WHERE EXTRACT(YEAR FROM sale_date) = p_year;

        SELECT product_name INTO v_top_product
        FROM sales
        WHERE EXTRACT(YEAR FROM sale_date) = p_year
        GROUP BY product_name
        ORDER BY SUM(amount) DESC
        LIMIT 1;

        SELECT COUNT(DISTINCT customer_id) INTO v_customer_count
        FROM sales
        WHERE EXTRACT(YEAR FROM sale_date) = p_year;

        -- 返回结果
        RAISE NOTICE 'Total sales: %', v_total_sales;
        RAISE NOTICE 'Top product: %', v_top_product;
        RAISE NOTICE 'Customers: %', v_customer_count;
    END;
    $$;
    """;

SQLStatement statement = parser.parse(sql);
CreateProcedureStmt proc = (CreateProcedureStmt) statement;
ProcedureBody body = proc.getBody();

System.out.println("语言: " + body.getLanguage());
System.out.println("源代码: " + body.getSourceCode());
```

#### 带控制流的存储过程

```java
String sql = """
    CREATE PROCEDURE process_refund(
        p_order_id IN INTEGER,
        p_reason IN VARCHAR,
        p_success OUT BOOLEAN
    )
    LANGUAGE plpgsql
    AS $$
    DECLARE
        v_order_status VARCHAR(20);
        v_refund_amount NUMERIC;
    BEGIN
        -- 检查订单状态
        SELECT status INTO v_order_status
        FROM orders
        WHERE id = p_order_id;

        IF v_order_status IS NULL THEN
            RAISE EXCEPTION 'Order % not found', p_order_id;
        ELSIF v_order_status != 'PAID' THEN
            RAISE EXCEPTION 'Order % is not paid', p_order_id;
        END IF;

        -- 计算退款金额
        SELECT SUM(amount) INTO v_refund_amount
        FROM order_items
        WHERE order_id = p_order_id;

        IF v_refund_amount = 0 THEN
            RAISE EXCEPTION 'No items to refund for order %', p_order_id;
        END IF;

        -- 执行退款
        BEGIN
            INSERT INTO refunds (order_id, amount, reason, created_at)
            VALUES (p_order_id, v_refund_amount, p_reason, CURRENT_TIMESTAMP);

            UPDATE orders SET status = 'REFUNDED' WHERE id = p_order_id;

            p_success := TRUE;

        EXCEPTION
            WHEN OTHERS THEN
                ROLLBACK;
                p_success := FALSE;
                RAISE;
        END;
    END;
    $$;
    """;

SQLStatement statement = parser.parse(sql);
```

### 4. CALL 语句

#### 位置参数调用

```java
import com.sdchat.ogsql.ast.CallFuncStmt;

String sql = "CALL calculate_bonus(1001, 0.15);";
SQLStatement statement = parser.parse(sql);

if (statement instanceof CallFuncStmt) {
    CallFuncStmt call = (CallFuncStmt) statement;
    System.out.println("调用的存储过程: " + call.getProcedureName());
    System.out.println("位置参数数量: " + call.getArguments().size());
}
```

#### 命名参数调用

```java
String sql = "CALL process_data(input => 'data', mode => 'full', retry => true);";
SQLStatement statement = parser.parse(sql);

CallFuncStmt call = (CallFuncStmt) statement;
System.out.println("命名参数: " + call.getNamedArguments());

for (Map.Entry<String, ValueExpression> entry : call.getNamedArguments().entrySet()) {
    System.out.println("  " + entry.getKey() + " => " + entry.getValue().getValue());
}
```

### 5. ALTER PROCEDURE

```java
import com.sdchat.ogsql.ast.AlterProcedureStmt;

// 重命名存储过程
String sql1 = "ALTER PROCEDURE old_name RENAME TO new_name;";
SQLStatement stmt1 = parser.parse(sql1);
AlterProcedureStmt alter1 = (AlterProcedureStmt) stmt1;
System.out.println("新名称: " + alter1.getNewName());

// 更改所有者
String sql2 = "ALTER PROCEDURE my_proc OWNER TO dbadmin;";
SQLStatement stmt2 = parser.parse(sql2);
AlterProcedureStmt alter2 = (AlterProcedureStmt) stmt2;
System.out.println("新所有者: " + alter2.getNewOwner());

// 更改 schema
String sql3 = "ALTER PROCEDURE my_proc SET SCHEMA public;";
SQLStatement stmt3 = parser.parse(sql3);
AlterProcedureStmt alter3 = (AlterProcedureStmt) stmt3;
System.out.println("新 schema: " + alter3.getNewSchema());

// 更改安全属性
String sql4 = "ALTER PROCEDURE my_proc SECURITY INVOKER;";
SQLStatement stmt4 = parser.parse(sql4);
AlterProcedureStmt alter4 = (AlterProcedureStmt) stmt4;
System.out.println("调用者权限: " + alter4.isSecurityInvokerSet());
```

### 6. DROP PROCEDURE

```java
// 删除存储过程
String sql1 = "DROP PROCEDURE test_proc;";
SQLStatement stmt1 = parser.parse(sql1);

// 删除多个存储过程
String sql2 = "DROP PROCEDURE proc1, proc2, proc3;";
SQLStatement stmt2 = parser.parse(sql2);

// 删除存储过程（如果存在）
String sql3 = "DROP PROCEDURE IF EXISTS temp_proc;";
SQLStatement stmt3 = parser.parse(sql3);
AlterProcedureStmt alter3 = (AlterProcedureStmt) stmt3;
System.out.println("新 schema: " + alter3.getNewSchema());

// 更改安全属性
String sql4 = "ALTER PROCEDURE my_proc SECURITY INVOKER;";
SQLStatement stmt4 = parser.parse(sql4);
AlterProcedureStmt alter4 = (AlterProcedureStmt) stmt4;
System.out.println("调用者权限: " + alter4.isSecurityInvokerSet());
```

### 7. 存储过程高级特性

#### 游标（CURSOR）操作

```java
/**
 * 游标示例：逐行处理查询结果
 */
String sql = """
    CREATE PROCEDURE process_active_users(
        p_batch_size IN INTEGER DEFAULT 100
    )
    LANGUAGE plpgsql
    SECURITY DEFINER
    AS $$
    DECLARE
        user_cursor CURSOR FOR
            SELECT id, name, email, last_login
            FROM users
            WHERE active = true
            ORDER BY last_login DESC
            LIMIT p_batch_size;
        v_user_id users.id%TYPE;
        v_user_name users.name%TYPE;
        v_user_email users.email%TYPE;
        v_processed_count INTEGER := 0;
    BEGIN
        -- 打开游标
        OPEN user_cursor;

        -- 循环获取游标中的每一行
        LOOP
            FETCH user_cursor INTO v_user_id, v_user_name, v_user_email;

            -- 检查是否所有行已处理
            EXIT WHEN NOT FOUND;

            -- 处理用户数据
            INSERT INTO user_processing_log (
                user_id,
                processed_at,
                status
            ) VALUES (
                v_user_id,
                CURRENT_TIMESTAMP,
                'PROCESSING'
            );

            v_processed_count := v_processed_count + 1;

            -- 每处理 10 行提交一次（避免事务过大）
            IF v_processed_count % 10 = 0 THEN
                COMMIT;
            END IF;
        END LOOP;

        -- 关闭游标
        CLOSE user_cursor;

        RAISE NOTICE 'Processed % users', v_processed_count;
    END;
    $$;
    """;

SQLStatement statement = parser.parse(sql);
CreateProcedureStmt proc = (CreateProcedureStmt) statement;
ProcedureBody body = proc.getBody();

// 获取源代码并分析游标定义
String sourceCode = body.getSourceCode();
if (sourceCode.contains("CURSOR FOR")) {
    System.out.println("存储过程包含游标定义");
}

// 提取变量声明
if (body.hasDeclarations()) {
    for (com.sdchat.ogsql.ast.VariableDeclaration decl : body.getDeclarations()) {
        System.out.println("变量声明: " + decl);
    }
}
```

#### FOR 循环遍历

```java
String sql = """
    CREATE PROCEDURE calculate_department_stats(
        p_department_id IN INTEGER
    )
    LANGUAGE plpgsql
    AS $$
    DECLARE
        v_total_salary NUMERIC := 0;
        v_employee_count INTEGER := 0;
        v_avg_salary NUMERIC;
    BEGIN
        -- 使用 FOR 循环直接遍历查询结果
        FOR emp_record IN
            SELECT salary
            FROM employees
            WHERE department_id = p_department_id
        LOOP
            v_total_salary := v_total_salary + emp_record.salary;
            v_employee_count := v_employee_count + 1;
        END LOOP;

        -- 计算平均薪资
        IF v_employee_count > 0 THEN
            v_avg_salary := v_total_salary / v_employee_count;
        END IF;

        -- 插入统计结果
        INSERT INTO department_statistics (
            department_id,
            employee_count,
            total_salary,
            avg_salary,
            calculated_at
        ) VALUES (
            p_department_id,
            v_employee_count,
            v_total_salary,
            v_avg_salary,
            CURRENT_TIMESTAMP
        );

        RETURN v_employee_count;
    END;
    $$;
    """;

SQLStatement statement = parser.parse(sql);
CreateProcedureStmt proc = (CreateProcedureStmt) statement;
System.out.println("存储过程名称: " + proc.getProcedureName());
```

#### 事务控制（COMMIT/ROLLBACK）

```java
String sql = """
    CREATE PROCEDURE process_order_batch(
        p_order_ids IN INTEGER[]
    )
    LANGUAGE plpgsql
    AS $$
    DECLARE
        v_success_count INTEGER := 0;
        v_failure_count INTEGER := 0;
        v_order_id INTEGER;
    BEGIN
        -- 处理每个订单（每个订单独立事务）
        FOREACH v_order_id IN ARRAY p_order_ids
        LOOP
            BEGIN
                -- 开始独立事务
                -- 检查订单是否可处理
                IF EXISTS (
                    SELECT 1 FROM orders
                    WHERE id = v_order_id AND status = 'PENDING'
                ) THEN
                    -- 更新订单状态
                    UPDATE orders
                    SET status = 'PROCESSING',
                        updated_at = CURRENT_TIMESTAMP
                    WHERE id = v_order_id;

                    -- 记录处理日志
                    INSERT INTO order_processing_history (
                        order_id,
                        status,
                        processed_at
                    ) VALUES (
                        v_order_id,
                        'PROCESSING',
                        CURRENT_TIMESTAMP
                    );

                    -- 提交事务
                    COMMIT;
                    v_success_count := v_success_count + 1;
                ELSE
                    -- 订单不存在或已处理，回滚
                    ROLLBACK;
                    v_failure_count := v_failure_count + 1;
                END IF;

            EXCEPTION
                WHEN OTHERS THEN
                    -- 捕获异常并回滚
                    ROLLBACK;
                    v_failure_count := v_failure_count + 1;

                    -- 记录错误
                    INSERT INTO error_log (
                        error_time,
                        error_message,
                        context
                    ) VALUES (
                        CURRENT_TIMESTAMP,
                        SQLERRM,
                        'process_order_batch: order_id=' || v_order_id
                    );
            END;
        END LOOP;

        -- 返回处理结果
        RAISE NOTICE 'Success: %, Failures: %', v_success_count, v_failure_count;

        -- 返回成功处理的订单数量
        RETURN v_success_count;
    END;
    $$;
    """;

SQLStatement statement = parser.parse(sql);
CreateProcedureStmt proc = (CreateProcedureStmt) statement;
System.out.println("存储过程包含事务控制语句");
```

### 8. 存储过程内 DML 语句分析

```java
import com.sdchat.ogsql.parser.MultiParseResult;

/**
 * 分析存储过程中包含的 DML 语句
 */
public class ProcedureDmlAnalyzer {

    public static void analyzeProcedureBody(String sql) {
        SQLParser parser = new SQLParser();
        SQLStatement statement = parser.parse(sql);

        if (!(statement instanceof CreateProcedureStmt)) {
            System.out.println("这不是存储过程语句");
            return;
        }

        CreateProcedureStmt proc = (CreateProcedureStmt) statement;
        ProcedureBody body = proc.getBody();

        // 获取存储过程源代码
        String sourceCode = body.getSourceCode();

        System.out.println("=== 存储过程 DML 分析 ===");
        System.out.println("过程名: " + proc.getProcedureName());
        System.out.println("语言: " + body.getLanguage());
        System.out.println("\n源代码片段:\n" + sourceCode.substring(0, Math.min(500, sourceCode.length())) + "...");

        // 分析包含的 DML 语句类型
        analyzeDmlStatements(sourceCode);
    }

    private static void analyzeDmlStatements(String sourceCode) {
        String[] dmlKeywords = {
            "INSERT INTO",
            "UPDATE ",
            "DELETE FROM",
            "SELECT ",
            "MERGE INTO"
        };

        String upperCode = sourceCode.toUpperCase();
        Map<String, Integer> dmlCounts = new HashMap<>();

        // 统计每种 DML 语句的数量
        for (String keyword : dmlKeywords) {
            int count = countOccurrences(upperCode, keyword);
            if (count > 0) {
                dmlCounts.put(keyword.trim(), count);
            }
        }

        // 输出 DML 语句统计
        if (!dmlCounts.isEmpty()) {
            System.out.println("\nDML 语句统计:");
            for (Map.Entry<String, Integer> entry : dmlCounts.entrySet()) {
                System.out.printf("  %s: %d\n", entry.getKey(), entry.getValue());
            }
        } else {
            System.out.println("\n未检测到 DML 语句（可能只包含控制流和变量操作）");
        }

        // 检测事务控制语句
        String[] transactionKeywords = {"COMMIT;", "ROLLBACK;", "BEGIN;", "BEGIN WORK;", "START TRANSACTION;"};
        boolean hasTransaction = false;
        for (String keyword : transactionKeywords) {
            if (upperCode.contains(keyword)) {
                hasTransaction = true;
                System.out.println("\n检测到事务控制语句: " + keyword.trim());
            }
        }
        if (!hasTransaction) {
            System.out.println("\n未检测到显式事务控制语句");
        }

        // 检测游标定义
        if (upperCode.contains("CURSOR FOR")) {
            System.out.println("\n检测到游标定义");
        }

        // 检测循环结构
        String[] loopKeywords = {"FOR ", "LOOP", "WHILE ", "REPEAT"};
        boolean hasLoop = false;
        for (String keyword : loopKeywords) {
            if (upperCode.contains(keyword)) {
                hasLoop = true;
                System.out.println("\n检测到循环结构: " + keyword.trim());
            }
        }
    }

    private static int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }

    public static void main(String[] args) {
        String complexProcedure = """
            CREATE OR REPLACE PROCEDURE complex_data_migration()
            LANGUAGE plpgsql
            SECURITY DEFINER
            AS $$
            DECLARE
                batch_cursor CURSOR FOR
                    SELECT id, data
                    FROM staging_table
                    WHERE processed = false;

                v_migrated_count INTEGER := 0;
            BEGIN
                -- 批量迁移数据
                FOR record IN SELECT id, data FROM staging_table WHERE processed = false LIMIT 1000
                LOOP
                    BEGIN
                        -- 插入到目标表
                        INSERT INTO production_table (id, data, migrated_at)
                        VALUES (record.id, record.data, CURRENT_TIMESTAMP);

                        -- 标记为已处理
                        UPDATE staging_table
                        SET processed = true
                        WHERE id = record.id;

                        v_migrated_count := v_migrated_count + 1;

                        -- 每 100 条提交一次
                        IF v_migrated_count % 100 = 0 THEN
                            COMMIT;
                        END IF;

                    EXCEPTION
                        WHEN OTHERS THEN
                            ROLLBACK;
                            RAISE NOTICE 'Failed to migrate record ID: %', record.id;
                    END;
                END LOOP;

                CLOSE batch_cursor;

                -- 记录迁移统计
                INSERT INTO migration_log (
                    migration_type,
                    record_count,
                    status,
                    completed_at
                ) VALUES (
                    'staging_to_production',
                    v_migrated_count,
                    'COMPLETED',
                    CURRENT_TIMESTAMP
                );
            END;
            $$;
            """;

        analyzeProcedureBody(complexProcedure);
    }
}
```

---

## 元数据提取

### 1. 提取表名

```java
import com.sdchat.ogsql.metadata.MetadataExtractor;
import com.sdchat.ogsql.ast.SelectQuery;

String sql = """
    SELECT u.name, o.order_id, p.product_name
    FROM users u
    INNER JOIN orders o ON u.id = o.user_id
    LEFT JOIN products p ON o.product_id = p.id
    WHERE u.active = true
    """;

SQLStatement statement = parser.parse(sql);
MetadataExtractor extractor = new MetadataExtractor();
extractor.extract(statement);

System.out.println("引用的表: " + extractor.getTables());
// 输出: [users, orders, products]
```

### 2. 提取列名

```java
String sql = "SELECT id, name, email, created_at FROM users WHERE active = true";

SQLStatement statement = parser.parse(sql);
MetadataExtractor extractor = new MetadataExtractor();
extractor.extract(statement);

System.out.println("列名: " + extractor.getColumns());
// 输出: [Column{id}, Column{name}, Column{email}, Column{created_at}]
```

### 3. 提取函数调用

```java
String sql = """
    SELECT
        COUNT(*) as total,
        AVG(price) as avg_price,
        SUM(amount) as total_amount,
        UPPER(name) as uppercase_name
    FROM products
    """;

SQLStatement statement = parser.parse(sql);
MetadataExtractor extractor = new MetadataExtractor();
extractor.extract(statement);

System.out.println("函数调用: " + extractor.getFunctions());
// 输出: [COUNT, AVG, SUM, UPPER]
```

### 4. 提取 WHERE 条件

```java
import com.sdchat.ogsql.metadata.Condition;

String sql = "SELECT * FROM orders WHERE status = 'completed' AND amount > 100 OR (created_at < '2020-01-01' AND priority = 'high')";

SQLStatement statement = parser.parse(sql);
MetadataExtractor extractor = new MetadataExtractor();
extractor.extract(statement);

System.out.println("WHERE 条件: " + extractor.getWhereConditions());
for (Condition cond : extractor.getWhereConditions()) {
    System.out.println("  " + cond.getOperator() + " " + cond.getLeft() + " " + cond.getRight());
}
```

---

## 错误处理

### 1. 语法错误

```java
import com.sdchat.ogsql.exception.ParseException;
import com.sdchat.ogsql.exception.SyntaxErrorException;

try {
    String sql = "SELEC id FROM users";  // 拼写错误
    SQLStatement statement = parser.parse(sql);
} catch (ParseException e) {
    System.out.println("解析错误: " + e.getMessage());

    // 语法错误提供详细信息
    if (e instanceof SyntaxErrorException) {
        SyntaxErrorException see = (SyntaxErrorException) e;
        System.out.println("错误位置: 行 " + see.getLine() + ", 列 " + see.getColumn());
        System.out.println("上下文: " + see.getContext());
        System.out.println("建议: " + see.getSuggestion());
    }
}
```

### 2. 语义错误

```java
import com.sdchat.ogsql.exception.SemanticErrorException;

try {
    String sql = "CALL my_proc(1 => 1, 2);";  // 混合使用位置和命名参数
    SQLStatement statement = parser.parse(sql);
} catch (SemanticErrorException e) {
    System.out.println("语义错误: " + e.getMessage());
    System.out.println("错误位置: 行 " + e.getLine());
    System.out.println("上下文: " + e.getContext());
}
```

### 3. 输入验证错误

```java
import com.sdchat.ogsql.exception.InputValidationException;

try {
    String hugeSql = "...".repeat(10000000);  // 超大 SQL
    SQLStatement statement = parser.parse(hugeSql);
} catch (InputValidationException e) {
    System.out.println("输入验证错误: " + e.getMessage());
}
```

### 4. 批量解析错误处理

```java
import com.sdchat.ogsql.parser.MultiParseResult;

String sql = """
    SELECT * FROM users;
    INSERT INTO;  -- 语法错误
    UPDATE users SET name = 'test';
    DELETe FROM logs;  -- 拼写错误
    """;

MultiParseResult result = parser.parseMultiple(sql);

System.out.println("成功解析: " + result.getStatements().size());
System.out.println("错误: " + result.getErrors().size());

if (result.hasErrors()) {
    for (ParseException error : result.getErrors()) {
        System.out.println("错误: " + error.getMessage());
        System.out.println("  位置: 行 " + (error instanceof SyntaxErrorException
                ? ((SyntaxErrorException) error).getLine() : "未知"));
    }
}
```

---

## 性能优化

### 1. 文件流式解析

```java
import java.io.File;
import java.io.FileInputStream;

// 解析大文件
File sqlFile = new File("large_queries.sql");

try {
    SQLStatement statement = parser.parseFile(sqlFile);
    System.out.println("成功解析文件");
} catch (InputValidationException e) {
    System.out.println("文件大小超限: " + e.getMessage());
} catch (IOException e) {
    System.out.println("文件读取错误: " + e.getMessage());
}
```

### 2. 配置解析限制

```java
// 创建解析器并配置
SQLParser parser = new SQLParser();

// 设置最大文件大小 (默认: 100MB)
parser.setMaxFileSize(50 * 1024 * 1024);  // 50MB

// 设置内存限制 (默认: 500MB)
parser.setMemoryLimit(200 * 1024 * 1024);  // 200MB

// 设置流缓冲区大小 (默认: 8KB)
parser.setStreamBufferSize(16 * 1024);  // 16KB

// 设置错误策略
parser.setErrorStrategy(ErrorStrategy.BAIL);  // 快速失败
// 或
parser.setErrorStrategy(ErrorStrategy.DEFAULT);  // 详细错误
```

### 3. 批量解析优化

```java
// 对于多个 SQL 语句，使用 parseMultiple() 而非循环调用 parse()
String multiSql = "SELECT 1; SELECT 2; SELECT 3;";

// 高效方式: 一次解析
MultiParseResult result = parser.parseMultiple(multiSql);

// 低效方式: 多次调用 (应避免)
for (String singleSql : multiSql.split(";")) {
    SQLStatement stmt = parser.parse(singleSql.trim());
}
```

---

## 最佳实践

### 1. 语句类型检查

```java
SQLStatement statement = parser.parse(sql);

switch (statement.getStatementType()) {
    case SELECT:
        handleSelect((SelectQuery) statement);
        break;
    case INSERT:
        handleInsert((InsertStatement) statement);
        break;
    case CREATE_PROCEDURE:
        handleCreateProcedure((CreateProcedureStmt) statement);
        break;
    case ALTER_PROCEDURE:
        handleAlterProcedure((AlterProcedureStmt) statement);
        break;
    case CALL_PROCEDURE:
        handleCallProcedure((CallFuncStmt) statement);
        break;
    default:
        System.out.println("未知语句类型: " + statement.getStatementType());
}
```

### 2. Visitor 模式遍历 AST

```java
import com.sdchat.ogsql.visitor.ASTVisitor;

public class SqlAnalyzer implements ASTVisitor<Void> {

    @Override
    public Void visitSelectQuery(SelectQuery select) {
        System.out.println("处理 SELECT 查询");
        // 自定义分析逻辑
        return null;
    }

    @Override
    public Void visitCreateProcedureStmt(CreateProcedureStmt proc) {
        System.out.println("处理存储过程: " + proc.getProcedureName());
        // 自定义分析逻辑
        return null;
    }

    // 实现其他 visit 方法...
}

// 使用 visitor
SQLStatement statement = parser.parse(sql);
SqlAnalyzer analyzer = new SqlAnalyzer();
statement.accept(analyzer);
```

### 3. 防御性编程

```java
// 检查集合是否为空
if (select.getColumns() != null && !select.getColumns().isEmpty()) {
    System.out.println("列数量: " + select.getColumns().size());
}

// 使用 has* 方法而不是检查集合大小
if (proc.hasParameters()) {
    for (ProcedureParameter param : proc.getParameters()) {
        // 处理参数
    }
}
```

### 4. 资源管理

```java
// 解析文件时使用 try-with-resources
try (FileInputStream fis = new FileInputStream(sqlFile)) {
    SQLStatement statement = parser.parseStream(fis);
    // 处理语句
} catch (IOException e) {
    System.out.println("文件处理错误");
}
```

### 5. 异常处理策略

```java
// 针对不同异常类型采取不同处理策略
try {
    SQLStatement statement = parser.parse(sql);
    processStatement(statement);
} catch (SyntaxErrorException e) {
    // 语法错误：记录并提示用户
    logger.error("SQL 语法错误: {}", e.getMessage());
    showSyntaxErrorToUser(e);
} catch (SemanticErrorException e) {
    // 语义错误：记录并建议修复
    logger.error("SQL 语义错误: {}", e.getMessage());
    suggestFix(e);
} catch (InputValidationException e) {
    // 输入验证错误：限制或拒绝
    logger.warn("输入验证失败: {}", e.getMessage());
    return ValidationResult.INVALID_INPUT;
} catch (Exception e) {
    // 未知错误：安全地回退
    logger.error("未预期的错误: {}", e.getMessage());
    throw new ProcessingException("无法处理 SQL", e);
}
```

---

## 完整示例：SQL 验证工具

```java
import com.sdchat.ogsql.parser.SQLParser;
import com.sdchat.ogsql.parser.MultiParseResult;
import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.metadata.MetadataExtractor;
import com.sdchat.ogsql.exception.ParseException;
import java.io.File;

public class SqlValidator {

    private final SQLParser parser = new SQLParser();

    /**
     * 验证 SQL 文件
     */
    public void validateFile(File sqlFile) {
        try {
            MultiParseResult result = parser.parseFile(sqlFile);

            System.out.println("=== SQL 文件验证结果 ===");
            System.out.println("文件: " + sqlFile.getName());
            System.out.println("成功解析: " + result.getStatements().size() + " 条语句");
            System.out.println("错误: " + result.getErrors().size());

            if (result.hasErrors()) {
                System.out.println("\n=== 错误详情 ===");
                for (ParseException error : result.getErrors()) {
                    System.out.println("错误: " + error.getMessage());
                }
            } else {
                System.out.println("\n=== SQL 语句统计 ===");
                analyzeStatements(result.getStatements());
            }

        } catch (Exception e) {
            System.out.println("文件处理失败: " + e.getMessage());
        }
    }

    /**
     * 分析 SQL 语句
     */
    private void analyzeStatements(List<SQLStatement> statements) {
        MetadataExtractor extractor = new MetadataExtractor();

        // 按类型统计
        Map<String, Integer> typeCount = new HashMap<>();
        Set<String> allTables = new HashSet<>();
        Set<String> allProcedures = new HashSet<>();

        for (SQLStatement stmt : statements) {
            String typeName = stmt.getStatementType().name();
            typeCount.put(typeName, typeCount.getOrDefault(typeName, 0) + 1);

            // 提取表名
            extractor.extract(stmt);
            allTables.addAll(extractor.getTables());

            // 记录存储过程
            if (stmt instanceof CreateProcedureStmt) {
                CreateProcedureStmt proc = (CreateProcedureStmt) stmt;
                allProcedures.add(proc.getProcedureName());
            }
        }

        // 输出统计
        System.out.println("\n语句类型:");
        for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
            System.out.printf("  %s: %d\n", entry.getKey(), entry.getValue());
        }

        System.out.println("\n引用的表 (" + allTables.size() + "):");
        for (String table : allTables) {
            System.out.println("  - " + table);
        }

        if (!allProcedures.isEmpty()) {
            System.out.println("\n定义的存储过程 (" + allProcedures.size() + "):");
            for (String proc : allProcedures) {
                System.out.println("  - " + proc);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("用法: java SqlValidator <sql-file>");
            return;
        }

        SqlValidator validator = new SqlValidator();
        File sqlFile = new File(args[0]);
        validator.validateFile(sqlFile);
    }
}
```

---

## 附录

### 支持的语句类型

| 语句类型 | 枚举值 | Java 类 |
|---------|----------|----------|
| SELECT | SELECT | SelectQuery |
| INSERT | INSERT | InsertStatement |
| UPDATE | UPDATE | UpdateStatement |
| DELETE | DELETE | DeleteStatement |
| CREATE TABLE | CREATE_TABLE | CreateStatement |
| ALTER TABLE | ALTER_TABLE | AlterStatement |
| DROP TABLE | DROP_TABLE | DropStatement |
| CREATE FOREIGN TABLE | CREATE_FOREIGN_TABLE | ExternalTable |
| CREATE PROCEDURE | CREATE_PROCEDURE | CreateProcedureStmt |
| ALTER PROCEDURE | ALTER_PROCEDURE | AlterProcedureStmt |
| CALL PROCEDURE | CALL_PROCEDURE | CallFuncStmt |

### 错误策略

| 策略 | 描述 | 适用场景 |
|------|------|---------|
| DEFAULT | 提供详细错误信息，尝试恢复 | 开发、调试 |
| BAIL | 第一个错误即停止 | 生产环境、快速失败 |

### 获取更多帮助

- 用户指南: `docs/user-guide/README.md`
- API 文档: 参考 Javadoc
- 示例代码: `docs/examples/`
- 测试: `src/test/java/`
