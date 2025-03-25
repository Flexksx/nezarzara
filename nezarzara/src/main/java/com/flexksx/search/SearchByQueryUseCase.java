package com.flexksx.search;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.flexksx.http.HttpRequester;

public class SearchByQueryUseCase {

    public List<SearchResult> execute(String searchTerm) throws IOException {
        String encodedTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        String searchUrl = "https://html.duckduckgo.com/html/?q=" + encodedTerm;

        String html = HttpRequester.fetch(searchUrl);
        return extractSearchResults(html);
    }

    private List<SearchResult> extractSearchResults(String html) {
        List<SearchResult> results = new ArrayList<>();

        // This regex captures:
        // 1) The link URL in href="..."
        // 2) The title inside the <a> tag
        // 3) Optionally the snippet in <div class="result__snippet">...
        Pattern pattern = Pattern.compile(
                "<a[^>]*class=\"result__a\"[^>]*href=\"([^\"]+)\"[^>]*>(.*?)</a>" +
                        "(?:.*?<div[^>]*class=\"result__snippet\"[^>]*>(.*?)</div>)?",
                Pattern.DOTALL);

        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String url = matcher.group(1).trim();
            String titleHtml = matcher.group(2);
            String snippetHtml = matcher.group(3);

            String title = stripHtml(titleHtml).trim();
            String snippet = snippetHtml == null ? "" : stripHtml(snippetHtml).trim();

            url = fixRedirectUrl(url);
            results.add(new SearchResult(title, url, snippet));
        }
        return results;
    }

    private String fixRedirectUrl(String url) {
        if (url.startsWith("//")) {
            url = "https:" + url;
        }
        Pattern redirectPattern = Pattern.compile(".*?uddg=([^&]+).*");
        Matcher m = redirectPattern.matcher(url);
        if (m.matches()) {
            String encoded = m.group(1);
            try {
                return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
            } catch (Exception e) {
                return url;
            }
        }
        return url;
    }

    private String stripHtml(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        return input.replaceAll("<[^>]+>", "");
    }

}
