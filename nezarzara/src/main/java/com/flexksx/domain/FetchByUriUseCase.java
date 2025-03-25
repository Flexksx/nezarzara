package com.flexksx.domain;

import com.flexksx.display.HtmlToMarkdownConvertor;
import com.flexksx.display.MarkdownTerminalRenderer;
import com.flexksx.http.HttpRequester;

public class FetchByUriUseCase {
    public static void execute(String uri) {
        final String failureMessage = new StringBuilder().append("Failed to fetch uri: ").append(uri).toString();
        try {
            String rawResponse = HttpRequester.fetch(uri);
            String markdownResponse = HtmlToMarkdownConvertor.convert(rawResponse);
            MarkdownTerminalRenderer.render(markdownResponse);
        } catch (Exception e) {
            System.err.println(failureMessage);
        }
    }
}