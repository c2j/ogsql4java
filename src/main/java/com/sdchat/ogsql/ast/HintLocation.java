package com.sdchat.ogsql.ast;

/**
 * Hint Location Information
 *
 * Stores the location of a hint within the original SQL statement.
 * This information can be used for error reporting and diagnostics.
 */
public class HintLocation {

    private final int line;
    private final int column;
    private final int startOffset;
    private final int endOffset;

    public HintLocation(int line, int column) {
        this(line, column, -1, -1);
    }

    public HintLocation(int line, int column, int startOffset, int endOffset) {
        this.line = line;
        this.column = column;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public boolean hasOffsets() {
        return startOffset >= 0 && endOffset >= 0;
    }

    @Override
    public String toString() {
        return "HintLocation{line=" + line + ", column=" + column +
               (hasOffsets() ? ", offset=" + startOffset + "-" + endOffset : "") + "}";
    }
}
