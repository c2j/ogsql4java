package com.sdchat.ogsql.graph;

/**
 * Enum representing different types of SQL JOIN operations.
 */
public enum JoinType {
    INNER_JOIN("INNER JOIN"),
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN"),
    FULL_JOIN("FULL JOIN"),
    CROSS_JOIN("CROSS JOIN");

    private final String displayName;

    JoinType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Parse join type from SQL keyword.
     */
    public static JoinType fromSql(String sql) {
        if (sql == null) {
            return INNER_JOIN;
        }
        String normalized = sql.toUpperCase().trim();
        
        switch (normalized) {
            case "LEFT":
            case "LEFT OUTER":
                return LEFT_JOIN;
            case "RIGHT":
            case "RIGHT OUTER":
                return RIGHT_JOIN;
            case "FULL":
            case "FULL OUTER":
                return FULL_JOIN;
            case "CROSS":
                return CROSS_JOIN;
            case "INNER":
            default:
                return INNER_JOIN;
        }
    }
}
