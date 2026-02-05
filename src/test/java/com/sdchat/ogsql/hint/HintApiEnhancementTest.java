package com.sdchat.ogsql.hint;

import com.sdchat.ogsql.ast.HintLocation;
import com.sdchat.ogsql.ast.PerformanceHint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Hint API enhancements.
 */
@DisplayName("Hint API Enhancement Tests")
public class HintApiEnhancementTest {

    private HintRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HintRegistry();
    }

    @Test
    @DisplayName("Should load hint registry successfully")
    void testLoadHintRegistry() {
        // When
        boolean loaded = registry.load();

        // Then
        assertTrue(loaded, "Registry should load successfully");
        assertTrue(registry.isLoaded(), "Registry should be marked as loaded");
    }

    @Test
    @DisplayName("Should validate valid hints")
    void testValidateValidHints() {
        // Given
        registry.load();

        // When & Then
        assertTrue(registry.isValidHint("NestLoop"), "NestLoop should be valid");
        assertTrue(registry.isValidHint("MergeJoin"), "MergeJoin should be valid");
        assertTrue(registry.isValidHint("HashJoin"), "HashJoin should be valid");
        assertTrue(registry.isValidHint("IndexScan"), "IndexScan should be valid");
    }

    @Test
    @DisplayName("Should reject invalid hints")
    void testValidateInvalidHints() {
        // Given
        registry.load();

        // When & Then
        assertFalse(registry.isValidHint("UnknownHint"), "Unknown hint should be invalid");
        assertFalse(registry.isValidHint("InvalidType"), "Invalid type should be rejected");
        assertFalse(registry.isValidHint(null), "Null hint should be invalid");
        assertFalse(registry.isValidHint(""), "Empty hint should be invalid");
    }

    @Test
    @DisplayName("Should get all valid hints")
    void testGetValidHints() {
        // Given
        registry.load();

        // When
        var hints = registry.getValidHints();

        // Then
        assertNotNull(hints, "Hints set should not be null");
        assertFalse(hints.isEmpty(), "Hints set should not be empty");
        assertTrue(hints.contains("NestLoop"), "Should contain NestLoop");
        assertTrue(hints.contains("HashJoin"), "Should contain HashJoin");
    }

    @Test
    @DisplayName("Should create successful validation result")
    void testCreateValidResult() {
        // When
        HintValidationResult result = HintValidationResult.valid("NestLoop");

        // Then
        assertTrue(result.isValid(), "Result should be valid");
        assertEquals("NestLoop", result.getHintType(), "Hint type should match");
        assertNull(result.getErrorMessage(), "Error message should be null for valid hint");
    }

    @Test
    @DisplayName("Should create failed validation result")
    void testCreateInvalidResult() {
        // When
        HintValidationResult result = HintValidationResult.invalid("UnknownHint", "Hint not found in knowledge base");

        // Then
        assertFalse(result.isValid(), "Result should be invalid");
        assertEquals("UnknownHint", result.getHintType(), "Hint type should match");
        assertEquals("Hint not found in knowledge base", result.getErrorMessage(), "Error message should match");
    }

    @Test
    @DisplayName("Should create hint with location")
    void testCreateHintWithLocation() {
        // Given
        HintLocation location = new HintLocation(10, 5);
        PerformanceHint hint = new PerformanceHint("NestLoop", Arrays.asList("users", "orders"));

        // When
        hint.setLocation(location);

        // Then
        assertEquals(location, hint.getLocation(), "Location should be set correctly");
        assertEquals(10, hint.getLocation().getLine(), "Line should be 10");
        assertEquals(5, hint.getLocation().getColumn(), "Column should be 5");
    }

    @Test
    @DisplayName("Should validate hint and set status")
    void testValidateHint() {
        // Given
        registry.load();
        PerformanceHint hint = new PerformanceHint("NestLoop", Arrays.asList("users", "orders"));

        // When
        boolean isValid = registry.isValidHint(hint.getHintType());
        hint.setValid(isValid);

        // Then
        assertTrue(hint.isValid(), "Hint should be valid");
        assertNull(hint.getErrorMessage(), "Error message should be null for valid hint");
    }

    @Test
    @DisplayName("Should handle invalid hint with error message")
    void testHandleInvalidHint() {
        // Given
        registry.load();
        PerformanceHint hint = new PerformanceHint("UnknownHint", Arrays.asList("users"));

        // When
        boolean isValid = registry.isValidHint(hint.getHintType());
        hint.setValid(isValid);
        if (!isValid) {
            hint.setErrorMessage("Hint '" + hint.getHintType() + "' not found in knowledge base");
        }

        // Then
        assertFalse(hint.isValid(), "Hint should be invalid");
        assertEquals("Hint 'UnknownHint' not found in knowledge base", hint.getErrorMessage(), "Error message should be set");
    }

    @Test
    @DisplayName("Should handle registry not loaded")
    void testRegistryNotLoaded() {
        // When registry is not loaded
        assertFalse(registry.isLoaded(), "Registry should not be loaded");

        // Then validation should return false
        assertFalse(registry.isValidHint("NestLoop"), "Should return false when registry not loaded");
        assertTrue(registry.getValidHints().isEmpty(), "Should return empty set when not loaded");
    }

    @Test
    @DisplayName("Should reload registry")
    void testReloadRegistry() {
        // Given
        boolean firstLoad = registry.load();
        assertTrue(firstLoad, "First load should succeed");

        // When
        boolean reloadSuccess = registry.reload();

        // Then
        assertTrue(reloadSuccess, "Reload should succeed");
        assertTrue(registry.isLoaded(), "Registry should still be loaded after reload");
    }
}