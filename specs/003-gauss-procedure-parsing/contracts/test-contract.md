# Test Contract: Gauss Stored Procedure Parser

**Feature**: Gauss Stored Procedure Parsing Enhancement
**Version**: 1.0.0
**Date**: 2025-01-15

## Overview

This contract defines the testing requirements for Gauss stored procedure parsing. All tests follow Test-Driven Development principles and must achieve 90% code coverage per Constitution Principle II.

---

## Test Structure

```
src/test/java/com/sdchat/ogsql/
├── contract/
│   ├── GrammarTest.java                 # Extended: Add procedure grammar tests
│   └── ProcedureParsingTest.java        # NEW: Contract tests for procedures
├── integration/
│   ├── ParseTest.java                   # Extended: Add procedure integration tests
│   └── ProcedureIntegrationTest.java    # NEW: End-to-end procedure parsing
└── unit/
    ├── ast/
    │   ├── CreateProcedureStmtTest.java      # NEW: Unit tests for AST nodes
    │   ├── AlterProcedureStmtTest.java       # NEW
    │   ├── CallFuncStmtTest.java             # NEW
    │   ├── ProcedureParameterTest.java       # NEW
    │   ├── ProcedureBodyTest.java            # NEW
    │   └── ProcedureSecurityTest.java        # NEW
    └── parser/
        └── ProcedureParserTest.java          # NEW: Parser unit tests
```

---

## Contract Tests

### GrammarTest.java (Extended)

**Package**: `com.sdchat.ogsql.contract`

**Purpose**: Verify grammar rules produce expected token sequences

**New Test Methods**:

```java
@Test
public void testCreateProcedureGrammar() {
    // Test token sequence for CREATE PROCEDURE
}

@Test
public void testCreateProcedureWithParameters() {
    // Test parameter list tokenization
}

@Test
public void testCreateProcedureWithOrReplace() {
    // Test OR REPLACE clause
}

@Test
public void testCreateProcedureWithSecurityDefiner() {
    // Test SECURITY DEFINER clause
}

@Test
public void testAlterProcedureRename() {
    // Test ALTER PROCEDURE ... RENAME TO
}

@Test
public void testAlterProcedureOwner() {
    // Test ALTER PROCEDURE ... OWNER TO
}

@Test
public void testCallStatementGrammar() {
    // Test CALL statement tokenization
}

@Test
public void testCallWithNamedParameters() {
    // Test CALL with named parameters (param => value)
}

@Test
public void testDropProcedureGrammar() {
    // Test DROP PROCEDURE tokenization
}

@Test
public void testDropProcedureIfExists() {
    // Test DROP PROCEDURE IF EXISTS
}

@Test
public void testDropProcedureCascade() {
    // Test DROP PROCEDURE ... CASCADE
}

@Test
public void testProcedureBodyGrammar() {
    // Test BEGIN/END block tokenization
}

@Test
public void testNestedBlocksGrammar() {
    // Test nested BEGIN/END blocks
}
```

---

### ProcedureParsingTest.java (NEW)

**Package**: `com.sdchat.ogsql.contract`

**Purpose**: Contract tests for procedure AST node round-tripping

**Test Methods**:

```java
public class ProcedureParsingTest {

    @Test
    public void testCreateProcedureASTStructure() {
        // Verify CreateProcedureStmt has correct structure
    }

    @Test
    public void testCreateProcedureRoundTrip() {
        // Parse SQL -> Create AST -> Generate SQL -> Parse again
        // Verify both parses produce identical AST
    }

    @Test
    public void testAlterProcedureASTStructure() {
        // Verify AlterProcedureStmt has correct structure
    }

    @Test
    public void testCallStatementASTStructure() {
        // Verify CallFuncStmt has correct structure
    }

    @Test
    public void testProcedureParameterModes() {
        // Test IN, OUT, INOUT, VARIADIC parameter modes
    }

    @Test
    public void testProcedureParameterDefaults() {
        // Test DEFAULT value parsing for parameters
    }

    @Test
    public void testProcedureSecurityAttributes() {
        // Test DEFINER, AUTHID, SECURITY INVOKER parsing
    }

    @Test
    public void testProcedureBodyStatements() {
        // Test procedural statements within body
    }

    @Test
    public void testProcedureExceptionHandlers() {
        // Test EXCEPTION blocks in procedure body
    }
}
```

---

## Integration Tests

### ParseTest.java (Extended)

**Package**: `com.sdchat.ogsql.integration`

**Purpose**: End-to-end parsing tests

**New Test Methods**:

```java
@Test
public void testCreateProcedureIntegration() {
    String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";
    ParseResult result = parser.parse(sql);
    assertTrue(result.containsProcedure());
    assertNotNull(result.getProcedureStatement());
}

@Test
public void testComplexProcedureIntegration() {
    String sql = """
        CREATE OR REPLACE PROCEDURE complex_proc(
            p1 IN INTEGER,
            p2 INOUT NUMERIC,
            p3 OUT VARCHAR,
            p4 IN INTEGER DEFAULT 0
        )
        LANGUAGE plpgsql
        SECURITY DEFINER
        AS $$
        DECLARE
            local_var INTEGER;
        BEGIN
            IF p1 > 0 THEN
                local_var := p1 * 2;
            ELSE
                local_var := 0;
            END IF;
            p2 := local_var + p4;
            p3 := 'Result: ' || local_var::text;
        EXCEPTION
            WHEN division_by_zero THEN
                p3 := 'Error: Division by zero';
        END;
        $$;
        """;
    ParseResult result = parser.parse(sql);
    CreateProcedureStmt stmt = (CreateProcedureStmt) result.getProcedureStatement();
    assertEquals(4, stmt.getParameters().size());
}
```

---

### ProcedureIntegrationTest.java (NEW)

**Package**: `com.sdchat.ogsql.integration`

**Purpose**: Comprehensive end-to-end tests for procedures

**Test Methods**:

```java
public class ProcedureIntegrationTest {

    @Test
    public void testSimpleCreateProcedure() {
        // Test basic procedure creation
    }

    @Test
    public void testCreateProcedureWithAllClauses() {
        // Test procedure with all clauses (OR REPLACE, parameters, SECURITY, etc.)
    }

    @Test
    public void testAlterProcedureScenarios() {
        // Test all ALTER PROCEDURE variations
    }

    @Test
    public void testCallStatementVariations() {
        // Test CALL with positional and named arguments
    }

    @Test
    public void testDropProcedureScenarios() {
        // Test DROP with IF EXISTS, CASCADE, RESTRICT
    }

    @Test
    public void testProcedureWithNestedBlocks() {
        // Test nested BEGIN/END blocks
    }

    @Test
    public void testProcedureWithLoops() {
        // Test FOR, WHILE, LOOP constructs
    }

    @Test
    public void testProcedureWithExceptionHandling() {
        // Test EXCEPTION blocks with multiple conditions
    }

    @Test
    public void testPerformanceParsingTime() {
        // Verify <50ms average parsing time (SC-005)
        long totalTime = 0;
        int iterations = 100;
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            parser.parseCreateProcedure(testSQL);
            totalTime += (System.nanoTime() - start);
        }
        double avgMs = (totalTime / iterations) / 1_000_000.0;
        assertTrue(avgMs < 50, "Average parsing time: " + avgMs + "ms");
    }

    @Test
    public void testMemoryUsage() {
        // Verify <20% memory increase (SC-006)
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
        List<CreateProcedureStmt> procedures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            procedures.add(parser.parseCreateProcedure(testSQL));
        }
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = afterMemory - beforeMemory;
        // Baseline would need to be measured; test verifies reasonable increase
        assertTrue(memoryIncrease < (beforeMemory * 0.2), "Memory increase too large");
    }

    @Test
    public void testErrorRecovery() {
        // Test that parser continues after errors
        String sql = """
            CREATE PROCEDURE bad_proc(p1 IN) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;
            CREATE PROCEDURE good_proc(p1 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;
            """;
        List<ParseResult> results = parser.parseBatch(sql);
        assertEquals(2, results.size());
        assertTrue(results.get(0).hasErrors());
        assertFalse(results.get(1).hasErrors());
    }

    @Test
    public void testCompatibilityModes() {
        // Test parsing in different compatibility modes (A, B, C, PG, D)
        for (String mode : Arrays.asList("A", "B", "C", "PG", "D")) {
            parser.setCompatibilityMode(mode);
            ParseResult result = parser.parse(testSQL);
            assertNotNull(result);
        }
    }
}
```

---

## Unit Tests

### CreateProcedureStmtTest.java (NEW)

**Package**: `com.sdchat.ogsql.unit.ast`

**Purpose**: Unit tests for CreateProcedureStmt class

**Test Methods**:

```java
public class CreateProcedureStmtTest {

    @Test
    public void testConstructorWithName() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        assertEquals("test_proc", stmt.getProcedureName());
        assertFalse(stmt.isOrReplace());
    }

    @Test
    public void testConstructorWithOrReplace() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc", true);
        assertEquals("test_proc", stmt.getProcedureName());
        assertTrue(stmt.isOrReplace());
    }

    @Test
    public void testAddParameters() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        ProcedureParameter param1 = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER");
        ProcedureParameter param2 = new ProcedureParameter("p2", ParameterMode.OUT, "VARCHAR");
        stmt.addParameter(param1);
        stmt.addParameter(param2);
        assertEquals(2, stmt.getParameters().size());
        assertEquals("p1", stmt.getParameters().get(0).getName());
    }

    @Test
    public void testSetBody() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        stmt.setBody(body);
        assertNotNull(stmt.getBody());
        assertEquals("plpgsql", body.getLanguage());
    }

    @Test
    public void testSetSecurity() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        ProcedureSecurity security = new ProcedureSecurity();
        security.setDefiner(true);
        stmt.setSecurity(security);
        assertNotNull(stmt.getSecurity());
        assertTrue(security.isDefiner());
    }

    @Test
    public void testVisitorAccept() {
        CreateProcedureStmt stmt = new CreateProcedureStmt("test_proc");
        MockVisitor visitor = new MockVisitor();
        stmt.accept(visitor);
        assertTrue(visitor.createProcedureVisited);
    }
}
```

### AlterProcedureStmtTest.java (NEW)

**Package**: `com.sdchat.ogsql.unit.ast`

**Purpose**: Unit tests for AlterProcedureStmt class

**Test Methods**:

```java
public class AlterProcedureStmtTest {

    @Test
    public void testRenameProcedure() {
        AlterProcedureStmt stmt = new AlterProcedureStmt("old_proc");
        stmt.setNewName("new_proc");
        assertEquals("old_proc", stmt.getProcedureName());
        assertEquals("new_proc", stmt.getNewName());
    }

    @Test
    public void testChangeOwner() {
        AlterProcedureStmt stmt = new AlterProcedureStmt("test_proc");
        stmt.setNewOwner("new_owner");
        assertEquals("new_owner", stmt.getNewOwner());
    }

    @Test
    public void testChangeSchema() {
        AlterProcedureStmt stmt = new AlterProcedureStmt("test_proc");
        stmt.setNewSchema("new_schema");
        assertEquals("new_schema", stmt.getNewSchema());
    }

    @Test
    public void testSecurityInvoker() {
        AlterProcedureStmt stmt = new AlterProcedureStmt("test_proc");
        stmt.setSecurityInvoker(true);
        assertTrue(stmt.getSecurityInvoker());
    }
}
```

### CallFuncStmtTest.java (NEW)

**Package**: `com.sdchat.ogsql.unit.ast`

**Purpose**: Unit tests for CallFuncStmt class

**Test Methods**:

```java
public class CallFuncStmtTest {

    @Test
    public void testPositionalArguments() {
        CallFuncStmt stmt = new CallFuncStmt("test_proc");
        stmt.addArgument(new LiteralExpression(1));
        stmt.addArgument(new LiteralExpression("hello"));
        assertEquals(2, stmt.getArguments().size());
        assertTrue(stmt.getNamedArguments().isEmpty());
    }

    @Test
    public void testNamedArguments() {
        CallFuncStmt stmt = new CallFuncStmt("test_proc");
        stmt.addNamedArgument("param1", new LiteralExpression(1));
        stmt.addNamedArgument("param2", new LiteralExpression("hello"));
        assertEquals(2, stmt.getNamedArguments().size());
        assertTrue(stmt.getArguments().isEmpty());
    }

    @Test
    public void testHasReturnValue() {
        CallFuncStmt stmt = new CallFuncStmt("test_proc");
        stmt.setHasReturnValue(true);
        assertTrue(stmt.hasReturnValue());
    }
}
```

### ProcedureParameterTest.java (NEW)

**Package**: `com.sdchat.ogsql.unit.ast`

**Purpose**: Unit tests for ProcedureParameter class

**Test Methods**:

```java
public class ProcedureParameterTest {

    @Test
    public void testParameterModes() {
        for (ParameterMode mode : ParameterMode.values()) {
            ProcedureParameter param = new ProcedureParameter("p1", mode, "INTEGER");
            assertEquals(mode, param.getMode());
        }
    }

    @Test
    public void testDefaultValue() {
        ProcedureParameter param = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER", new LiteralExpression(0), 0);
        assertNotNull(param.getDefaultValue());
        assertEquals(0, param.getPosition());
    }

    @Test
    public void testEquality() {
        ProcedureParameter param1 = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER");
        ProcedureParameter param2 = new ProcedureParameter("p1", ParameterMode.IN, "INTEGER");
        assertEquals(param1, param2);
        assertEquals(param1.hashCode(), param2.hashCode());
    }
}
```

### ProcedureBodyTest.java (NEW)

**Package**: `com.sdchat.ogsql.unit.ast`

**Purpose**: Unit tests for ProcedureBody class

**Test Methods**:

```java
public class ProcedureBodyTest {

    @Test
    public void testBodyConstructor() {
        ProcedureBody body = new ProcedureBody("plpgsql", "$$ BEGIN NULL; END; $$");
        assertEquals("plpgsql", body.getLanguage());
        assertEquals("$$ BEGIN NULL; END; $$", body.getSourceCode());
    }

    @Test
    public void testDeclarations() {
        ProcedureBody body = new ProcedureBody("plpgsql", "");
        VariableDeclaration decl = new VariableDeclaration("var1", "INTEGER");
        body.addDeclaration(decl);
        assertEquals(1, body.getDeclarations().size());
    }

    @Test
    public void testStatements() {
        ProcedureBody body = new ProcedureBody("plpgsql", "");
        Statement stmt = new NullStatement();
        body.addStatement(stmt);
        assertEquals(1, body.getStatements().size());
    }

    @Test
    public void testExceptionHandlers() {
        ProcedureBody body = new ProcedureBody("plpgsql", "");
        ExceptionHandler handler = new ExceptionHandler(Arrays.asList("division_by_zero"), new ArrayList<>());
        body.addExceptionHandler(handler);
        assertEquals(1, body.getExceptionHandlers().size());
    }
}
```

### ProcedureSecurityTest.java (NEW)

**Package**: `com.sdchat.ogsql.unit.ast`

**Purpose**: Unit tests for ProcedureSecurity class

**Test Methods**:

```java
public class ProcedureSecurityTest {

    @Test
    public void testAuthidTypes() {
        ProcedureSecurity security = new ProcedureSecurity();
        security.setAuthid(AuthidType.DEFINER);
        assertEquals(AuthidType.DEFINER, security.getAuthid());

        security.setAuthid(AuthidType.CURRENT_USER);
        assertEquals(AuthidType.CURRENT_USER, security.getAuthid());
    }

    @Test
    public void testDefiner() {
        ProcedureSecurity security = new ProcedureSecurity();
        security.setDefiner(true);
        assertTrue(security.isDefiner());
    }

    @Test
    public void testSecurityInvoker() {
        ProcedureSecurity security = new ProcedureSecurity();
        security.setSecurityInvoker(true);
        assertTrue(security.isSecurityInvoker());
    }
}
```

---

## Error Handling Tests

### SyntaxErrorTests

**Package**: `com.sdchat.ogsql.unit.exception` (NEW)

```java
public class SyntaxErrorTests {

    @Test
    public void testMissingClosingParenthesis() {
        String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";
        assertThrows(SyntaxErrorException.class, () -> parser.parseCreateProcedure(sql));
    }

    @Test
    public void testInvalidParameterMode() {
        String sql = "CREATE PROCEDURE test_proc(p1 INVALID_MODE INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";
        assertThrows(SyntaxErrorException.class, () -> parser.parseCreateProcedure(sql));
    }

    @Test
    public void testVariadicNotLast() {
        String sql = "CREATE PROCEDURE test_proc(p1 VARIADIC INTEGER[], p2 IN INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";
        SyntaxErrorException ex = assertThrows(SyntaxErrorException.class, () -> parser.parseCreateProcedure(sql));
        assertTrue(ex.getMessage().contains("VARIADIC parameter must be the last"));
    }

    @Test
    public void testErrorPosition() {
        String sql = "CREATE PROCEDURE test_proc(p1 IN, p2 INTEGER) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";
        try {
            parser.parseCreateProcedure(sql);
            fail("Expected SyntaxErrorException");
        } catch (SyntaxErrorException e) {
            assertEquals(1, e.getLineNumber());
            // Column number depends on exact position
        }
    }
}
```

### SemanticErrorTests

**Package**: `com.sdchat.ogsql.unit.exception` (NEW)

```java
public class SemanticErrorTests {

    @Test
    public void testDuplicateParameterNames() {
        String sql = "CREATE PROCEDURE test_proc(p1 IN INTEGER, p1 IN VARCHAR) AS $$ BEGIN NULL; END; $$ LANGUAGE plpgsql;";
        assertThrows(SemanticErrorException.class, () -> parser.parseCreateProcedure(sql));
    }

    @Test
    public void testUnsupportedLanguage() {
        String sql = "CREATE PROCEDURE test_proc() LANGUAGE unsupported AS $$ BEGIN NULL; END; $$;";
        // May throw SemanticErrorException or warning depending on implementation
    }

    @Test
    public void testUnbalancedBlocks() {
        String sql = "CREATE PROCEDURE test_proc() AS $$ BEGIN BEGIN NULL; END; $$ LANGUAGE plpgsql;";
        assertThrows(SemanticErrorException.class, () -> parser.parseCreateProcedure(sql));
    }
}
```

---

## Coverage Requirements

### Minimum Coverage Targets

| Component | Coverage Target | Rationale |
|-----------|----------------|-----------|
| AST Node Classes | 95% | Pure logic, minimal external dependencies |
| Parser Classes | 90% | Constitution requirement (Testing Gates) |
| Grammar Rules | 85% | Covered via contract tests |
| Exception Classes | 90% | Error handling is critical |

### Coverage Measurement

- Tool: JaCoCo (already in use per AGENTS.md)
- Command: `mvn clean test jacoco:report`
- Report: `target/site/jacoco/index.html`
- Gate: Coverage must be ≥90% for parser components

---

## Test Data

### Test SQL Samples

**Simple Procedure**:
```sql
CREATE PROCEDURE simple_proc(p1 IN INTEGER) AS $$
BEGIN
    NULL;
END;
$$ LANGUAGE plpgsql;
```

**Complex Procedure**:
```sql
CREATE OR REPLACE PROCEDURE complex_proc(
    p1 IN INTEGER DEFAULT 0,
    p2 INOUT NUMERIC,
    p3 OUT VARCHAR
)
LANGUAGE plpgsql
SECURITY DEFINER
AUTHID DEFINER
AS $$
DECLARE
    local_var INTEGER;
    counter INTEGER := 0;
BEGIN
    FOR i IN 1..10 LOOP
        counter := counter + i;
        IF counter > 50 THEN
            EXIT;
        END IF;
    END LOOP;

    p2 := counter + p1;
    p3 := 'Count: ' || counter::text;
EXCEPTION
    WHEN division_by_zero THEN
        p3 := 'Error: Division by zero';
    WHEN OTHERS THEN
        p3 := 'Error: ' || SQLERRM;
END;
$$;
```

**CALL Statements**:
```sql
CALL simple_proc(123);
CALL complex_proc(p1 => 10, p2 => NULL, p3 => NULL);
```

**ALTER PROCEDURE**:
```sql
ALTER PROCEDURE simple_proc RENAME TO renamed_proc;
ALTER PROCEDURE complex_proc OWNER TO new_user;
ALTER PROCEDURE complex_proc SET SCHEMA new_schema;
```

**DROP PROCEDURE**:
```sql
DROP PROCEDURE simple_proc;
DROP PROCEDURE IF EXISTS complex_proc CASCADE;
```

---

## Test Execution

### Running All Tests

```bash
mvn test
```

### Running Procedure-Specific Tests

```bash
mvn test -Dtest="*Procedure*"
```

### Running with Coverage

```bash
mvn clean test jacoco:report
```

### Running Individual Test Classes

```bash
mvn test -Dtest="CreateProcedureStmtTest"
mvn test -Dtest="ProcedureIntegrationTest"
```
