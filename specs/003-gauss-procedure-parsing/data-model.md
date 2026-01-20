### AlterProcedureStmt

**Package**: `com.sdchat.ogsql.ast`

**Extends**: `SQLStatement`

**Fields**:
| Field | Type | Description | Validation |
|-------|------|-------------|------------|
| `procedureName` | `String` | Fully-qualified procedure name | Required, non-empty, valid identifier |
| `newName` | `String` | New procedure name (for RENAME TO) | Optional, valid identifier if present |
| `newOwner` | `String` | New owner name (for OWNER TO) | Optional, valid identifier if present |
| 'securityInvoker' | `Boolean` | SECURITY INVOKER setting (null = no change) | Optional, null-allowed |

**Validation Rules** (from FR-005):
- Exactly one modification action must be specified (RENAME, OWNER, SET SCHEMA, SECURITY, or DEPENDS)
- 'procedureName' must be a valid SQL identifier
- Optional fields must be null if their corresponding clause is not specified

**Note**: The parser rule is `alterprocedurestmt` (lowercase 'a') which matches ALTERPROCEDURE token case, creating an AlterProcedureStmt AST node with capital 'A'.
