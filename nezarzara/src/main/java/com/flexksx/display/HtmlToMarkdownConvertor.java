package com.flexksx.display;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

public class HtmlToMarkdownConvertor {

    public static String convert(String html) {
        String markdown = FlexmarkHtmlConverter.builder().build().convert(html);
        markdown = postProcess(markdown);
        return markdown;
    }

    private static String postProcess(String markdown) {
        markdown = markdown.replaceAll("<[^>]+>", "");
        StringBuilder sb = new StringBuilder();
        String[] lines = markdown.split("\\r?\\n");
        for (String line : lines) {
            if (line.trim().startsWith("*")) {
                sb.append(line).append("\n");
            } else {
                line = line.replaceAll("([^\\n])\\s*(\\[[^\\]]+\\]\\([^\\)]+\\))", "$1\n$2");
                line = line.replaceAll("(\\][^\\n])\\s*(\\[)", "$1\n$2");
                sb.append(line).append("\n");
            }
        }
        markdown = sb.toString();
        markdown = markdown.replaceAll("[\\n]{2,}", "\n\n");
        return markdown.trim();
    }
}
