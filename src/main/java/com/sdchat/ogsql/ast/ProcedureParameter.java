package com.sdchat.ogsql.ast;

/**
 * Represents a parameter definition in a stored procedure.
 * 
 * <p>Procedure parameters have a mode (IN, OUT, INOUT, or VARIADIC),
 * a data type, an optional default value, and a position in the parameter list.</p>
 * 
 * <p>Parameter modes:</p>
 * <ul>
 *   <li>IN - Input parameter (default)</li>
 *   <li>OUT - Output parameter</li>
 *   <li>INOUT - Both input and output</li>
 *   <li>VARIADIC - Variable number of arguments (must be last)</li>
 * </ul>
 * 
 * @see CreateProcedureStmt
 */
public class ProcedureParameter {

    /**
     * Parameter modes for procedure parameters.
     */
    public enum ParameterMode {
        IN, OUT, INOUT, VARIADIC
    }

    private String name;
    private ParameterMode mode;
    private String dataType;
    private ValueExpression defaultValue;
    private int position;

    public ProcedureParameter(String name, ParameterMode mode, String dataType) {
        this.name = name;
        this.mode = mode;
        this.dataType = dataType;
        this.defaultValue = null;
        this.position = 0;
    }

    public ProcedureParameter(String name, ParameterMode mode, String dataType, ValueExpression defaultValue, int position) {
        this.name = name;
        this.mode = mode;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParameterMode getMode() {
        return mode;
    }

    public void setMode(ParameterMode mode) {
        this.mode = mode;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public ValueExpression getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(ValueExpression defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProcedureParameter that = (ProcedureParameter) obj;
        return position == that.position &&
               mode == that.mode &&
               name.equals(that.name) &&
               dataType.equals(that.dataType);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + mode.hashCode();
        result = 31 * result + dataType.hashCode();
        result = 31 * result + position;
        return result;
    }
}
