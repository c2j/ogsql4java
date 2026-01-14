package com.sdchat.ogsql.parser;

import com.sdchat.ogsql.ast.SQLStatement;
import com.sdchat.ogsql.grammar.*;
import com.sdchat.ogsql.exception.SyntaxErrorException;
import com.sdchat.ogsql.exception.InputValidationException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Main SQL parser entry point for OpenGauss SQL statements.
 * Provides high-level parsing interface with error handling and configuration options.
 */
public class SQLParser {

    // Configuration constants
    public static final long DEFAULT_MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB
    public static final long DEFAULT_MEMORY_LIMIT = 500 * 1024 * 1024; // 500MB
    public static final int DEFAULT_STREAM_BUFFER_SIZE = 8192; // 8KB
    
    private ErrorStrategy errorStrategy = ErrorStrategy.DEFAULT;
    private long maxFileSize = DEFAULT_MAX_FILE_SIZE;
    private long memoryLimit = DEFAULT_MEMORY_LIMIT;
    private int streamBufferSize = DEFAULT_STREAM_BUFFER_SIZE;
    
    public SQLParser() {
    }
    
    public void setErrorStrategy(ErrorStrategy strategy) {
        this.errorStrategy = strategy;
    }
    
    /**
     * Sets the maximum file size for parsing (default: 100MB).
     * 
     * @param maxFileSize Maximum file size in bytes
     */
    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
    
    /**
     * Sets the memory limit for parsing (default: 500MB).
     * 
     * @param memoryLimit Memory limit in bytes
     */
    public void setMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
    }
    
    /**
     * Sets the stream buffer size for large file parsing (default: 8KB).
     * 
     * @param streamBufferSize Buffer size in bytes
     */
    public void setStreamBufferSize(int streamBufferSize) {
        this.streamBufferSize = streamBufferSize;
    }
    
    /**
     * Parses SQL from a potentially large file using streaming.
     * 
     * @param file The file to parse
     * @return ParseResult containing the parsed statements
     * @throws InputValidationException if file size exceeds limit
     * @throws IOException if file reading fails
     */
    public ParseResult parseFile(File file) throws InputValidationException, IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        
        // Check file size
        if (file.length() > maxFileSize) {
            throw new InputValidationException(
                String.format("File size %d bytes exceeds maximum allowed size of %d bytes", 
                             file.length(), maxFileSize)
            );
        }
        
        // Monitor memory usage
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        if (usedMemory > memoryLimit) {
            throw new InputValidationException(
                String.format("Current memory usage %d bytes exceeds limit of %d bytes", 
                             usedMemory, memoryLimit)
            );
        }
        
        // Use streaming for large files
        try (InputStream inputStream = new FileInputStream(file);
             BufferedInputStream bufferedStream = new BufferedInputStream(inputStream, streamBufferSize)) {
            
            return parseStream(bufferedStream);
        }
    }
    
    /**
     * Parses SQL from an input stream.
     * 
     * @param inputStream The input stream to parse
     * @return ParseResult containing the parsed statements
     * @throws IOException if stream reading fails
     */
    public ParseResult parseStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        
        // Read the entire stream into a string for now
        // In a production implementation, we might use a more sophisticated streaming approach
        String sql = readStreamToString(inputStream);
        
        try {
            SQLStatement statement = parse(sql);
            return new ParseResult(statement);
        } catch (Exception e) {
            ParsingError error = new ParsingError(e.getMessage(), 0, 0, com.sdchat.ogsql.exception.ErrorSeverity.ERROR);
            return new ParseResult(error);
        }
    }
    
    /**
     * Reads an input stream to a string with size validation.
     * 
     * @param inputStream The input stream to read
     * @return The string content
     * @throws IOException if reading fails
     * @throws InputValidationException if content exceeds size limit
     */
    private String readStreamToString(InputStream inputStream) throws IOException, InputValidationException {
        StringBuilder result = new StringBuilder();
        byte[] buffer = new byte[streamBufferSize];
        int bytesRead;
        long totalBytesRead = 0;
        
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            totalBytesRead += bytesRead;
            if (totalBytesRead > maxFileSize) {
                throw new InputValidationException(
                    String.format("Input stream size %d bytes exceeds maximum allowed size of %d bytes", 
                                 totalBytesRead, maxFileSize)
                );
            }
            result.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
        }
        
        return result.toString();
    }

    public SQLStatement parse(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new com.sdchat.ogsql.exception.ParseException("SQL statement cannot be null or empty");
        }

        // Handle comment-only input gracefully
        String trimmedSql = sql.trim();
        if (trimmedSql.startsWith("--") || (trimmedSql.startsWith("/*") && trimmedSql.contains("*/"))) {
            // For comment-only input, return a simple placeholder statement
            return new com.sdchat.ogsql.ast.SelectQuery(); // Return empty SELECT as placeholder
        }

        CharStream input = CharStreams.fromString(sql);
        OpenGaussSQLLexer lexer = new OpenGaussSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OpenGaussSQLParser parser = new OpenGaussSQLParser(tokens);

        if (errorStrategy == ErrorStrategy.BAIL) {
            parser.setErrorHandler(new BailErrorStrategy());
        }

        SyntaxErrorListener errorListener = new SyntaxErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        try {
            OpenGaussSQLParser.RootContext tree = parser.root();
            ASTBuilder visitor = new ASTBuilder();
            SQLStatement statement = tree.accept(visitor);

            if (errorListener.hasErrors()) {
                throw errorListener.getFirstError();
            }

            return statement;
        } catch (ParseCancellationException e) {
            // Handle bail strategy cancellation
            if (errorListener.hasErrors()) {
                throw errorListener.getFirstError();
            }
            throw new com.sdchat.ogsql.exception.ParseException("Parsing cancelled due to syntax error: " + e.getMessage());
        }
    }
    
    public List<SQLStatement> parseMultiple(String sql) {
        if (sql == null) {
            return new ArrayList<>(); // Return empty list for null input
        }
        
        String trimmed = sql.trim();
        if (trimmed.isEmpty()) {
            return new ArrayList<>(); // Return empty list for empty input
        }

        List<SQLStatement> statements = new ArrayList<>();
        String[] individualStatements = sql.split(";");
        
        for (String statement : individualStatements) {
            String trimmedStatement = statement.trim();
            if (!trimmedStatement.isEmpty() && !isCommentOnly(trimmedStatement)) {
                try {
                    SQLStatement parsedStatement = parse(trimmedStatement);
                    if (parsedStatement != null) {
                        statements.add(parsedStatement);
                    }
                } catch (Exception e) {
                    // Continue parsing other statements even if one fails
                    // In a production system, you might want to collect errors
                }
            }
        }
        
        return statements;
    }
    
    private boolean isCommentOnly(String sql) {
        return sql.startsWith("--") || sql.startsWith("/*");
    }
    
    private static class SyntaxErrorListener extends BaseErrorListener {
        private final List<SyntaxErrorException> errors = new ArrayList<>();

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                               int line, int charPositionInLine,
                               String msg, RecognitionException e) {
            String errorInfo = String.format("Syntax error at line %d:%d - %s", line, charPositionInLine, msg);
            
            // Extract error context (50 characters around error)
            String context = extractErrorContext(recognizer, offendingSymbol, charPositionInLine);
            
            // Generate error suggestions
            String suggestion = generateErrorSuggestion(msg, offendingSymbol);
            
            errors.add(new SyntaxErrorException(errorInfo, line, charPositionInLine, context, suggestion));
        }

        private String extractErrorContext(Recognizer<?, ?> recognizer, Object offendingSymbol, int charPositionInLine) {
            if (!(recognizer instanceof Parser)) {
                return null;
            }
            
            Parser parser = (Parser) recognizer;
            TokenStream tokens = parser.getTokenStream();
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
            int startIndex = Math.max(0, tokenIndex - 2);
            int stopIndex = Math.min(tokens.size() - 1, tokenIndex + 2);
            
            StringBuilder context = new StringBuilder();
            for (int i = startIndex; i <= stopIndex; i++) {
                Token token = tokens.get(i);
                if (token.getType() != Token.EOF) {
                    context.append(token.getText()).append(" ");
                }
            }
            
            return context.toString().trim();
        }
        
        private String generateErrorSuggestion(String msg, Object offendingSymbol) {
            if (msg == null) return null;
            
            String lowerMsg = msg.toLowerCase();
            
            // Check for specific SQL keywords in the original SQL (not just error message)
            if (offendingSymbol != null && offendingSymbol instanceof Token) {
                Token token = (Token) offendingSymbol;
                String tokenText = token.getText().toLowerCase();
                
                if (tokenText.contains("selec")) {
                    return "Did you mean 'SELECT'?";
                } else if (tokenText.contains("insrt") || tokenText.contains("inser")) {
                    return "Did you mean 'INSERT'?";
                } else if (tokenText.contains("updte") || tokenText.contains("updat")) {
                    return "Did you mean 'UPDATE'?";
                } else if (tokenText.contains("delte") || tokenText.contains("delet")) {
                    return "Did you mean 'DELETE'?";
                } else if (tokenText.contains("creat")) {
                    return "Did you mean 'CREATE'?";
                } else if (tokenText.contains("fromt") || tokenText.contains("fro")) {
                    return "Did you mean 'FROM'?";
                } else if (tokenText.contains("wher")) {
                    return "Did you mean 'WHERE'?";
                } else if (tokenText.contains("alte")) {
                    return "Did you mean 'ALTER'?";
                }
            }
            
            // Fallback to message-based detection
            if (lowerMsg.contains("missing") && lowerMsg.contains("from")) {
                return "Did you mean to add a FROM clause?";
            } else if (lowerMsg.contains("missing") && lowerMsg.contains("where")) {
                return "Did you mean to add a WHERE clause?";
            } else if (lowerMsg.contains("extraneous") && lowerMsg.contains("foreign")) {
                return "Check if FOREIGN keyword is in the correct position";
            } else if (lowerMsg.contains("mismatched") && lowerMsg.contains("expecting")) {
                // Check for common misspellings in error message
                if (lowerMsg.contains("selec")) {
                    return "Did you mean 'SELECT'?";
                } else if (lowerMsg.contains("inser") || lowerMsg.contains("insrt")) {
                    return "Did you mean 'INSERT'?";
                } else if (lowerMsg.contains("updat")) {
                    return "Did you mean 'UPDATE'?";
                } else if (lowerMsg.contains("delet")) {
                    return "Did you mean 'DELETE'?";
                } else if (lowerMsg.contains("creat")) {
                    return "Did you mean 'CREATE'?";
                } else if (lowerMsg.contains("fromt") || lowerMsg.contains("fro")) {
                    return "Did you mean 'FROM'?";
                } else if (lowerMsg.contains("wher")) {
                    return "Did you mean 'WHERE'?";
                } else if (lowerMsg.contains("alte")) {
                    return "Did you mean 'ALTER'?";
                }
                return "Check your SQL syntax - there may be a typo";
            }
            
            return "Check your SQL syntax";
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public SyntaxErrorException getFirstError() {
            return errors.isEmpty() ? null : errors.get(0);
        }
    }
}