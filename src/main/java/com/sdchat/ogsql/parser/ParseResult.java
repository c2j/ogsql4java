package com.sdchat.ogsql.parser;

import com.sdchat.ogsql.ast.SQLStatement;

/**
 * Wrapper class for parsing results.
 */
public class ParseResult {
    private final SQLStatement statement;
    private final ParsingError error;
    private final boolean success;

    public ParseResult(SQLStatement statement) {
        this.statement = statement;
        this.error = null;
        this.success = true;
    }

    public ParseResult(ParsingError error) {
        this.statement = null;
        this.error = error;
        this.success = false;
    }

    public SQLStatement getStatement() {
        return statement;
    }

    public ParsingError getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}