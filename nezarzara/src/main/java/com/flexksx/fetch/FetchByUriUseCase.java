package com.flexksx.fetch;

import com.flexksx.display.HtmlToMarkdownConvertor;
import com.flexksx.display.MarkdownTerminalRenderer;
import com.flexksx.http.HttpRequester;

public class FetchByUriUseCase {

    public static void execute(String uri) {
        final String failureMessage = "Failed to fetch uri: " + uri;
        try {
            String rawResponse = HttpRequester.fetch(uri);

            int headerEnd = rawResponse.indexOf("\r\n\r\n");
            if (headerEnd == -1) {
                headerEnd = rawResponse.indexOf("\n\n");
            }
            String headersStr;
            String bodyStr;
            if (headerEnd < 0) {
                headersStr = "";
                bodyStr = rawResponse;
            } else {
                headersStr = rawResponse.substring(0, headerEnd);
                bodyStr = rawResponse.substring(headerEnd + 4);
            }

            String contentType = findHeaderValue(headersStr, "Content-Type").toLowerCase();

            if (contentType.contains("application/json")) {
                String prettyJson = prettyPrintJson(bodyStr);
                MarkdownTerminalRenderer.render(prettyJson);

            } else if (contentType.contains("text/html")) {
                String markdownResponse = HtmlToMarkdownConvertor.convert(bodyStr);
                MarkdownTerminalRenderer.render(markdownResponse);

            } else {
                MarkdownTerminalRenderer.render(bodyStr);
            }

        } catch (Exception e) {
            System.err.println(failureMessage + "\nReason: " + e.getMessage());
        }
    }

    private static String findHeaderValue(String headers, String headerName) {
        String[] lines = headers.split("\\r?\\n");
        for (String line : lines) {
            // e.g. "Content-Type: text/html"
            int colonIdx = line.indexOf(':');
            if (colonIdx > 0) {
                String name = line.substring(0, colonIdx).trim();
                if (name.equalsIgnoreCase(headerName)) {
                    return line.substring(colonIdx + 1).trim();
                }
            }
        }
        return "";
    }

    private static String prettyPrintJson(String json) {
        StringBuilder sb = new StringBuilder();
        int indent = 0;
        boolean inString = false;

        for (char c : json.toCharArray()) {
            switch (c) {
                case '{':
                case '[':
                    sb.append(c);
                    if (!inString) {
                        sb.append("\n");
                        indent++;
                        sb.append("  ".repeat(indent));
                    }
                    break;
                case '}':
                case ']':
                    if (!inString) {
                        sb.append("\n");
                        indent = Math.max(0, indent - 1);
                        sb.append("  ".repeat(indent));
                    }
                    sb.append(c);
                    break;
                case ',':
                    sb.append(c);
                    if (!inString) {
                        sb.append("\n");
                        sb.append("  ".repeat(indent));
                    }
                    break;
                case ':':
                    sb.append(c);
                    if (!inString) {
                        sb.append(" ");
                    }
                    break;
                case '"':
                    sb.append(c);
                    inString = !inString;
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
