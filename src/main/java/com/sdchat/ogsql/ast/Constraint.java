package com.sdchat.ogsql.ast;

/**
 * Represents a table constraint (PRIMARY KEY, FOREIGN KEY, etc.).
 */
public class Constraint {
    private String type;
    private String name;
    private String definition;

    public Constraint() {
    }

    public Constraint(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
