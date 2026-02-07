package com.sdchat.ogsql.graph.formatter;

import com.sdchat.ogsql.graph.TableRelationshipGraph;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating graph formatters.
 * Supports multiple output formats and allows registration of custom formatters.
 */
public class FormatterFactory {
    
    private static final Map<String, GraphFormatter<?>> FORMATTERS = new HashMap<>();
    
    static {
        // Register built-in formatters
        registerFormatter(new DotFormatter());
        registerFormatter(new JsonFormatter());
    }
    
    /**
     * Register a custom formatter.
     * 
     * @param formatter the formatter to register
     */
    public static void registerFormatter(GraphFormatter<?> formatter) {
        FORMATTERS.put(formatter.getFormatType().toLowerCase(), formatter);
        // Also register by file extension
        FORMATTERS.put(formatter.getFileExtension().toLowerCase(), formatter);
    }
    
    /**
     * Get a formatter by format type or file extension.
     * 
     * @param format the format type (e.g., "text/vnd.graphviz", "application/json") 
     *               or file extension (e.g., "dot", "json")
     * @return the formatter, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <T> GraphFormatter<T> getFormatter(String format) {
        if (format == null) {
            return null;
        }
        return (GraphFormatter<T>) FORMATTERS.get(format.toLowerCase());
    }
    
    /**
     * Get the DOT formatter.
     * 
     * @return DotFormatter instance
     */
    public static DotFormatter getDotFormatter() {
        return (DotFormatter) FORMATTERS.get("dot");
    }
    
    /**
     * Get the JSON formatter.
     * 
     * @return JsonFormatter instance
     */
    public static JsonFormatter getJsonFormatter() {
        return (JsonFormatter) FORMATTERS.get("json");
    }
    
    /**
     * Check if a formatter is available for the given format.
     * 
     * @param format the format type or file extension
     * @return true if a formatter is available
     */
    public static boolean hasFormatter(String format) {
        if (format == null) {
            return false;
        }
        return FORMATTERS.containsKey(format.toLowerCase());
    }
    
    /**
     * Get all available format types.
     * 
     * @return array of available format types
     */
    public static String[] getAvailableFormats() {
        return new String[]{"dot", "json"};
    }
    
    /**
     * Format a graph using the specified format.
     * Convenience method that gets the formatter and applies it.
     * 
     * @param graph the graph to format
     * @param format the format type or file extension
     * @param <T> the output type
     * @return the formatted output, or null if formatter not found
     */
    public static <T> T format(TableRelationshipGraph graph, String format) {
        GraphFormatter<T> formatter = getFormatter(format);
        if (formatter == null) {
            return null;
        }
        return formatter.format(graph);
    }
}
