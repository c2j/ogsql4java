package com.sdchat.ogsql.parser;

import com.sdchat.ogsql.exception.SyntaxErrorException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 * Error listener for procedure-specific parsing errors.
 * Provides context-aware error messages for stored procedure syntax.
 */
public class ProcedureErrorListener extends BaseErrorListener {

    private final java.util.List<SyntaxErrorException> errors = new java.util.ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                          Object offendingSymbol,
                          int line,
                          int charPositionInLine,
                          String msg,
                          RecognitionException e) {

        String errorInfo = String.format("Syntax error at line %d:%d - %s", line, charPositionInLine, msg);

        String context = extractErrorContext(recognizer, offendingSymbol, charPositionInLine);

        String suggestion = generateProcedureErrorSuggestion(msg, offendingSymbol);

        SyntaxErrorException error = new SyntaxErrorException(errorInfo, line, charPositionInLine, context, suggestion);
        errors.add(error);
    }

    /**
     * Extracts the error context (SQL snippet around the error).
     *
     * @param recognizer The ANTLR4 recognizer
     * @param offendingSymbol The offending symbol
     * @param charPositionInLine Character position in line
     * @return Error context string
     */
    private String extractErrorContext(Recognizer<?, ?> recognizer, Object offendingSymbol, int charPositionInLine) {
        if (!(recognizer instanceof org.antlr.v4.runtime.Parser)) {
            return null;
        }

        org.antlr.v4.runtime.Parser parser = (org.antlr.v4.runtime.Parser) recognizer;
        org.antlr.v4.runtime.TokenStream tokens = parser.getTokenStream();

        if (tokens == null) {
            return null;
        }

        Token offendingToken = null;
        if (offendingSymbol instanceof Token) {
            offendingToken = (Token) offendingSymbol;
        }

        if (offendingToken == null) {
            return null;
        }

        int tokenIndex = offendingToken.getTokenIndex();
        int startIndex = Math.max(0, tokenIndex - 3);
        int stopIndex = Math.min(tokens.size() - 1, tokenIndex + 3);

        StringBuilder context = new StringBuilder();
        for (int i = startIndex; i <= stopIndex; i++) {
            Token token = tokens.get(i);
            if (token.getType() != Token.EOF) {
                context.append(token.getText()).append(" ");
            }
        }

        return context.toString().trim();
    }

    /**
     * Generates error suggestions specific to procedure parsing.
     *
     * @param msg The error message
     * @param offendingSymbol The offending symbol
     * @return Suggestion string
     */
    private String generateProcedureErrorSuggestion(String msg, Object offendingSymbol) {
        if (msg == null) {
            return null;
        }

        String lowerMsg = msg.toLowerCase();

        if (offendingSymbol != null && offendingSymbol instanceof Token) {
            Token token = (Token) offendingSymbol;
            String tokenText = token.getText().toLowerCase();

            if (tokenText.contains("proced")) {
                return "Did you mean 'PROCEDURE'?";
            } else if (tokenText.contains("func") && lowerMsg.contains("call")) {
                return "In CALL statements, use procedure name without 'FUNCTION' keyword";
            } else if (tokenText.contains("variad") && !lowerMsg.contains("last")) {
                return "VARIADIC parameters must be the last parameter";
            } else if (lowerMsg.contains("inout") || lowerMsg.contains("in out")) {
                return "Use 'INOUT' without spaces for input/output parameters";
            }
        }

        if (lowerMsg.contains("missing") && lowerMsg.contains("begin")) {
            return "Procedure body must be enclosed in BEGIN ... END block";
        } else if (lowerMsg.contains("missing") && lowerMsg.contains("end")) {
            return "Procedure body must be enclosed in BEGIN ... END block";
        } else if (lowerMsg.contains("variadic") && lowerMsg.contains("last")) {
            return "VARIADIC parameter must be the last parameter in the list";
        } else if (lowerMsg.contains("parameter mode")) {
            return "Valid parameter modes are: IN, OUT, INOUT, VARIADIC";
        } else if (lowerMsg.contains("security") && lowerMsg.contains("definer")) {
            return "Use 'SECURITY DEFINER' clause for execution context";
        }

        return "Check your stored procedure syntax";
    }

    /**
     * Checks if any errors were encountered during parsing.
     *
     * @return true if errors exist, false otherwise
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Gets the first error encountered.
     *
     * @return First SyntaxErrorException, or null if no errors
     */
    public SyntaxErrorException getFirstError() {
        return errors.isEmpty() ? null : errors.get(0);
    }

    /**
     * Gets all errors encountered during parsing.
     *
     * @return List of SyntaxErrorException objects
     */
    public java.util.List<SyntaxErrorException> getAllErrors() {
        return new java.util.ArrayList<>(errors);
    }
}
