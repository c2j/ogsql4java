package com.sdchat.ogsql.hint;

import com.sdchat.ogsql.ast.HintLocation;
import com.sdchat.ogsql.ast.PerformanceHint;

/**
 * Simple test to verify Hint API enhancements.
 */
public class HintApiDemo {

    public static void main(String[] args) {
        System.out.println("=== Hint API Enhancement Demo ===\n");

        // 1. Test HintRegistry
        System.out.println("1. Testing HintRegistry:");
        HintRegistry registry = new HintRegistry();
        boolean loaded = registry.load();
        System.out.println("   Registry loaded: " + loaded);
        System.out.println("   Registry is loaded: " + registry.isLoaded());
        
        // Test validation
        String[] testHints = {"NestLoop", "HashJoin", "UnknownHint", ""};
        for (String hint : testHints) {
            boolean valid = registry.isValidHint(hint);
            System.out.println("   Hint '" + hint + "' is valid: " + valid);
        }
        
        System.out.println("   Total valid hints: " + registry.getValidHints().size());

        // 2. Test HintValidationResult
        System.out.println("\n2. Testing HintValidationResult:");
        HintValidationResult validResult = HintValidationResult.valid("NestLoop");
        HintValidationResult invalidResult = HintValidationResult.invalid("BadHint", "Hint not found in knowledge base");
        
        System.out.println("   Valid result: " + validResult);
        System.out.println("   Invalid result: " + invalidResult);

        // 3. Test HintLocation
        System.out.println("\n3. Testing HintLocation:");
        HintLocation location = new HintLocation(10, 5);
        HintLocation locationWithOffset = new HintLocation(10, 5, 100, 120);
        
        System.out.println("   Location: " + location);
        System.out.println("   Location with offset: " + locationWithOffset);
        System.out.println("   Has offsets: " + locationWithOffset.hasOffsets());

        // 4. Test enhanced PerformanceHint
        System.out.println("\n4. Testing enhanced PerformanceHint:");
        PerformanceHint hint = new PerformanceHint("NestLoop", java.util.Arrays.asList("users", "orders"));
        hint.setLocation(location);
        
        // Validate and set status
        boolean isValid = registry.isValidHint(hint.getHintType());
        hint.setValid(isValid);
        if (!isValid) {
            hint.setErrorMessage("Hint '" + hint.getHintType() + "' not found in knowledge base");
        }
        
        System.out.println("   Hint type: " + hint.getHintType());
        System.out.println("   Hint tables: " + hint.getTables());
        System.out.println("   Hint location: " + hint.getLocation());
        System.out.println("   Hint is valid: " + hint.isValid());
        System.out.println("   Hint error message: " + hint.getErrorMessage());

        System.out.println("\n=== Demo completed successfully! ===");
    }
}