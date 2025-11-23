### Bug 原因

这个 Bug 是由 [XmlUtils.java](cci:7://file:///e:/project/intellij-javadocs/src/main/java/com/github/setial/intellijjavadocs/utils/XmlUtils.java:0:0-0:0) 中的 [normalizeTemplate](cci:1://file:///e:/project/intellij-javadocs/src/main/java/com/github/setial/intellijjavadocs/utils/XmlUtils.java:34:4-42:5) 方法引起的。

原本的代码逻辑是：

1. 创建一个 JDOM 的 XML [Element](cci:1://file:///e:/project/intellij-javadocs/src/main/java/com/github/setial/intellijjavadocs/utils/XmlUtils.java:44:4-55:5)。
2. 将模板内容作为该 Element 的内容。
3. 调用 `element.getTextNormalize()` 获取文本。

**问题在于 `getTextNormalize()` 方法**：它的设计初衷是规范化 XML 文本，这包括“压缩”空白字符（例如将多个空格合并为一个，或者在某些符号周围调整空格）。当你的日期格式中包含 `/` 或 `-` 等符号时，JDOM 的规范化逻辑错误地认为需要在这些符号周围调整空格，从而导致了 `2025 /11/23` 这种多余空格的情况。

### 如何修复

修复方法非常直接：**移除了 JDOM 的规范化逻辑**。

我们只需要保留原本逻辑中“处理换行符”的部分即可，不需要通过 XML 解析器来处理纯文本模板。

修改后的代码如下：

```java
public static String normalizeTemplate(String template) {
    // 直接替换字符串中的转义换行符，不再使用 element.getTextNormalize()
    return template.replaceAll("\\\\n", "\n");
}
```

这样修改后，模板中的内容（包括日期格式）将原封不动地保留，只处理换行符，从而解决了多余空格的问题。