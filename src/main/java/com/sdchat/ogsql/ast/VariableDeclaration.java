package com.sdchat.ogsql.ast;

/**
 * Represents a variable declaration within a stored procedure.
 * 
 * <p>Variables are declared in the DECLARE section of a procedure
 * and have a name and data type. They can be used throughout
 * the procedure body for temporary storage and computation.</p>
 * 
 * <p>Example:</p>
 * <pre>{@code
 * DECLARE
 *     v_count INTEGER;
 *     v_message VARCHAR(100);
 * BEGIN
 *     v_count := 0;
 * END;
 * }</pre>
 * 
 * @see ProcedureBody
 */
public class VariableDeclaration {
    private String name;
    private String dataType;

    public VariableDeclaration(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
