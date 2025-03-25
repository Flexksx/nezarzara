package com.flexksx.domain;

import com.flexksx.http.HttpRequester;

public class SearchByUriUseCase {
    public static String execute(String uri) {
        final String failureMessage = new StringBuilder().append("Failed to fetch uri: ").append(uri).toString();
        try {
            String rawResponse = HttpRequester.fetch(uri);
            return rawResponse;
        } catch (Exception e) {
            System.err.println("Got an error when trying to fetch URI: " + uri);
            return failureMessage;
        }
    }
}