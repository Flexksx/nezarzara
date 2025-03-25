package com.flexksx.commands;

import com.flexksx.fetch.FetchByUriUseCase;

public class UrlCommand implements Command {

    @Override
    public void run(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: No URL provided. Usage: go2web -u <URL>");
            return;
        }

        String url = args[0];

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        FetchByUriUseCase.execute(url);
    }
}
