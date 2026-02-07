package com.sdchat.ogsql.graph.formatter;

import com.sdchat.ogsql.graph.TableRelationshipGraph;

/**
 * Generic formatter interface for converting table relationship graphs to various output formats.
 * 
 * @param <T> The output type (e.g., String for DOT/JSON, byte[] for binary formats)
 */
public interface GraphFormatter<T> {
    
    /**
     * Format the table relationship graph into the target format.
     * 
     * @param graph the graph to format
     * @return the formatted output
     */
    T format(TableRelationshipGraph graph);
    
    /**
     * Get the MIME type or format identifier for this formatter.
     * 
     * @return format identifier (e.g., "text/vnd.graphviz", "application/json")
     */
    String getFormatType();
    
    /**
     * Get the file extension for this format.
     * 
     * @return file extension (e.g., "dot", "json")
     */
    String getFileExtension();
}
