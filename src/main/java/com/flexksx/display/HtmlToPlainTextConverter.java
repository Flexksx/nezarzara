package com.flexksx.display;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlToPlainTextConverter {

    /**
     * Converts HTML content into a plain-text, human-readable format.
     *
     * @param html the raw HTML content as a String.
     * @return formatted plain text extracted from the HTML.
     */
    public static String convert(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }

        String cleanedHtml = html.replaceAll("(?is)<script.*?>.*?</script>", "");
        cleanedHtml = cleanedHtml.replaceAll("(?is)<style.*?>.*?</style>", "");
        cleanedHtml = cleanedHtml.replaceAll("(?is)<head.*?>.*?</head>", "");
        cleanedHtml = cleanedHtml.replaceAll("(?is)<meta.*?>", "");
        cleanedHtml = cleanedHtml.replaceAll("(?is)<link.*?>", "");

        StringBuilder sb = new StringBuilder();

        Pattern titlePattern = Pattern.compile("(?is)<title.*?>(.*?)</title>");
        Matcher titleMatcher = titlePattern.matcher(cleanedHtml);
        if (titleMatcher.find()) {
            sb.append("Title: ").append(cleanText(titleMatcher.group(1))).append("\n\n");
        }

        for (int i = 1; i <= 6; i++) {
            Pattern headingPattern = Pattern.compile("(?is)<h" + i + ".*?>(.*?)</h" + i + ">");
            Matcher headingMatcher = headingPattern.matcher(cleanedHtml);
            while (headingMatcher.find()) {
                sb.append(cleanText(headingMatcher.group(1))).append("\n\n");
            }
        }

        Pattern pPattern = Pattern.compile("(?is)<p.*?>(.*?)</p>");
        Matcher pMatcher = pPattern.matcher(cleanedHtml);
        while (pMatcher.find()) {
            sb.append(cleanText(pMatcher.group(1))).append("\n\n");
        }

        Pattern liPattern = Pattern.compile("(?is)<li.*?>(.*?)</li>");
        Matcher liMatcher = liPattern.matcher(cleanedHtml);
        while (liMatcher.find()) {
            sb.append("• ").append(cleanText(liMatcher.group(1))).append("\n");
        }

        if (sb.length() < 50) {
            String fallback = cleanedHtml.replaceAll("(?is)<[^>]+>", " ");
            fallback = fallback.replaceAll("\\s+", " ").trim();
            sb.append(fallback);
        }

        return sb.toString().trim();
    }

    /**
     * Cleans a text snippet by removing any remaining HTML tags,
     * decoding common HTML entities, and normalizing whitespace.
     *
     * @param text the text snippet to clean.
     * @return a cleaned version of the text.
     */
    private static String cleanText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        text = text.replaceAll("(?is)<[^>]+>", " ");

        text = text.replace("&nbsp;", " ");
        text = text.replace("&amp;", "&");
        text = text.replace("&lt;", "<");
        text = text.replace("&gt;", ">");
        text = text.replace("&quot;", "\"");
        text = text.replace("&apos;", "'");

        text = text.replaceAll("\\s+", " ").trim();
        return text;
    }

}
