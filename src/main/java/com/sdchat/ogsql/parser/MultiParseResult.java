package com.sdchat.ogsql.parser;

import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.exception.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiParseResult {
    private final List<SQLStatement> statements;
    private final List<ParseException> errors;

    public MultiParseResult(List<SQLStatement> statements, List<ParseException> errors) {
        this.statements = statements != null ? statements : new ArrayList<>();
        this.errors = errors != null ? errors : new ArrayList<>();
    }

    public List<SQLStatement> getStatements() {
        return Collections.unmodifiableList(statements);
    }

    public List<ParseException> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasStatements() {
        return !statements.isEmpty();
    }
}
