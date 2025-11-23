package com.github.setial.intellijjavadocs.utils;

import org.jdom.Element;

public class JdomTest {

    public static void main(String[] args) {
        test("2025/11/23");
        test("2025-2032");
        test("2025.11.23");
        test("/**\n * @date: 2025/11/23\n */");
    }

    private static void test(String input) {
        Element element = new Element("template");
        element.addContent(input);
        String normalized = element.getTextNormalize();
        System.out.println("Input: [" + input.replace("\n", "\\n") + "]");
        System.out.println("Output: [" + normalized + "]");
        System.out.println("--------------------------------------------------");
    }
}
