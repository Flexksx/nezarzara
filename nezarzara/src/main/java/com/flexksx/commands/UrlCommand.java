package com.flexksx.commands;

import com.flexksx.domain.FetchByUriUseCase;

/**
 * Fetches the content from a given URL and displays it in the terminal
 * in a more human-readable (Markdown-rendered) format.
 */
public class UrlCommand implements Command {

    @Override
    public void run(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: No URL provided. Usage: go2web -u <URL>");
            return;
        }

        // The first arg is the URL
        String url = args[0];
        // Optionally, check if the user forgot "http://" or "https://" –
        // but your code may already handle that
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        FetchByUriUseCase.execute(url);
    }
}
