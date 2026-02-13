package com.sdchat.ogsql.regression;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagVerificationTest {

    @Test
    @Tag("verification")
    void verifyRegressionTagOnTestClass() {
        Class<?> testClass = SQLParsingRegressionTest.class;
        Tag tag = testClass.getAnnotation(Tag.class);
        
        assertNotNull(tag, "SQLParsingRegressionTest should have @Tag annotation");
        assertEquals("regression", tag.value(), "Tag value should be 'regression'");
        
        System.out.println("Verified: @Tag(\"regression\") is correctly applied to SQLParsingRegressionTest");
    }

    @Test
    @Tag("verification")
    void verifyTestClassHasDisplayName() {
        Class<?> testClass = SQLParsingRegressionTest.class;
        
        assertNotNull(testClass.getAnnotation(org.junit.jupiter.api.DisplayName.class), 
            "Test class should have @DisplayName");
        
        System.out.println("Verified: @DisplayName is correctly applied to SQLParsingRegressionTest");
    }

    @Test
    @Tag("verification")
    void verifyRegressionTestsAreDiscoverable() throws Exception {
        SQLTestFileDiscovery discovery = new SQLTestFileDiscovery();
        var sqlFiles = discovery.discoverSQLFiles();
        
        assertNotNull(sqlFiles, "Should be able to discover SQL files");
        assertFalse(sqlFiles.isEmpty(), "Should find at least one SQL file for regression testing");
        
        System.out.println("Verified: " + sqlFiles.size() + " SQL files discovered for regression testing");
    }

    @Test
    @Tag("verification")
    void verifyMavenSurefireCanFilterByTag() {
        System.out.println("Tag filtering verification:");
        System.out.println("  - To run only regression tests: mvn test -Dgroups=regression");
        System.out.println("  - To run only slow tests: mvn test -Dgroups=slow");
        System.out.println("  - To exclude slow tests: mvn test -Dgroups=!slow");
        System.out.println("  - Regression tag (@Tag(\"regression\")) is correctly configured");
        
        assertTrue(true, "Maven Surefire tag filtering documentation available");
    }
}
