package com.sdchat.ogsql.regression;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLTestFileDiscoveryTest {

    @Test
    void testSanitizeTestName() {
        SQLTestFileDiscovery discovery = new SQLTestFileDiscovery();

        assertEquals("select_basic", discovery.sanitizeTestName("select_basic.sql"));
        assertEquals("insert_update_006", discovery.sanitizeTestName("insert_update_006.sql"));
        assertEquals("test_file_name", discovery.sanitizeTestName("test-file-name.sql"));
        assertEquals("test_file_name", discovery.sanitizeTestName("test@file.name.sql"));
    }

    @Test
    void testDiscoverSQLFiles() throws IOException {
        SQLTestFileDiscovery discovery = new SQLTestFileDiscovery();
        List<SQLTestFileDiscovery.SQLFileInfo> files = discovery.discoverSQLFiles();

        assertNotNull(files);
        assertTrue(files.size() > 0, "Should discover at least one SQL file");

        SQLTestFileDiscovery.SQLFileInfo firstFile = files.get(0);
        assertNotNull(firstFile.getFileName());
        assertNotNull(firstFile.getPath());
        assertTrue(firstFile.getSize() >= 0);

        assertTrue(firstFile.getFileName().endsWith(".sql"));
    }

    @Test
    void testSQLFileInfo() {
        Path path = Path.of("tests/regress/sql/test.sql");
        SQLTestFileDiscovery.SQLFileInfo fileInfo = new SQLTestFileDiscovery.SQLFileInfo("test.sql", path, 1024);

        assertEquals("test.sql", fileInfo.getFileName());
        assertEquals(path, fileInfo.getPath());
        assertEquals(1024, fileInfo.getSize());

        String toString = fileInfo.toString();
        assertTrue(toString.contains("test.sql"));
        assertTrue(toString.contains("path"));
        assertTrue(toString.contains("size=1024"));
    }
}
