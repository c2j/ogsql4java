package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class AlterStatement implements SQLStatement {
    private String objectType;
    private String objectName;
    private List<AlterCommand> alterCommands = new ArrayList<>();

    @Override
    public StatementType getStatementType() {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List<AlterCommand> getAlterCommands() {
        return alterCommands;
    }
}
