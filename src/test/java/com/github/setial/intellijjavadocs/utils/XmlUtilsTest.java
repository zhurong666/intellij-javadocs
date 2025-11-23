package com.github.setial.intellijjavadocs.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlUtilsTest {

    @Test
    public void testNormalizeTemplate() {
        String input = "2025/11/23";
        String actual = XmlUtils.normalizeTemplate(input);
        System.out.println("Input: '" + input + "'");
        System.out.println("Actual: '" + actual + "'");
        assertEquals(input, actual);
    }

    @Test
    public void testNormalizeTemplateWithHyphen() {
        String input = "2025-2032";
        String actual = XmlUtils.normalizeTemplate(input);
        System.out.println("Input: '" + input + "'");
        System.out.println("Actual: '" + actual + "'");
        assertEquals(input, actual);
    }

    @Test
    public void testNormalizeTemplateFull() {
        String input = "/**\n * @date: 2025/11/23\n */";
        String actual = XmlUtils.normalizeTemplate(input);
        System.out.println("Input: '" + input + "'");
        System.out.println("Actual: '" + actual + "'");
        assertEquals(input, actual);
    }

    @Test
    public void testNormalizeTemplateUserExample() {
        // This simulates the user's reported output where spaces are added
        String input = "/**\n * @date: 2025/11/23 19:21\n */";
        String actual = XmlUtils.normalizeTemplate(input);
        System.out.println("Input: '" + input + "'");
        System.out.println("Actual: '" + actual + "'");

        // If the bug exists, actual will contain "2025 /11/23"
        if (actual.contains("2025 /")) {
            throw new RuntimeException("Found extra space: " + actual);
        }
        assertEquals(input, actual);
    }
}
