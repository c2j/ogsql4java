package com.sdchat.ogsql.ast;

import com.sdchat.ogsql.visitor.ASTVisitor;
import java.util.ArrayList;
import java.util.Collections;
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
        return visitor.visitAlterStatement(this);
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

    /**
     * Adds an alter command to the list of alter commands.
     *
     * @param command the alter command to add, cannot be null or empty
     * @throws IllegalArgumentException if the command is null or empty
     */
    public void addAlterCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            throw new IllegalArgumentException("Alter command cannot be null or empty");
        }
        if (alterCommands == null) {
            alterCommands = new ArrayList<>();
        }
        AlterCommand alterCommand = new AlterCommand();
        alterCommand.setDefinition(command);
        alterCommands.add(alterCommand);
    }

    public List<AlterCommand> getAlterCommands() {
        return Collections.unmodifiableList(alterCommands);
    }
}
