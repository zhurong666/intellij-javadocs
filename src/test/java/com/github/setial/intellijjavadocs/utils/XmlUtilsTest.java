package com.github.setial.intellijjavadocs.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlUtilsTest {

    @Test
    public void testNormalizeTemplate_DateWithSlashes() {
        String input = "/**\n" +
                " * @version v2.3\n" +
                " * @author: zhuyun\n" +
                " * @date: 2025/11/23 19:21\n" +
                " * @Description: Create order req\n" +
                " * @Copyright: 2025-2032 wgcloud. All rights reserved.\n" +
                " */";

        String expected = input; // We expect it to remain unchanged (except maybe newline normalization if any)

        // The method does replaceAll("\\\\n", "\n"), but our input already has \n.
        // Wait, the input to normalizeTemplate comes from writer.toString().
        // If the template has \n, FreeMarker outputs newlines.

        String result = XmlUtils.normalizeTemplate(input);

        // We want to see if spaces are inserted.
        // Using assertEquals to see the difference.
        assertEquals(expected, result);
    }
}
