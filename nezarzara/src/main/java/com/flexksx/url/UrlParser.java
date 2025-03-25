package com.flexksx.url;

public class UrlParser {

    public static ParsedUrl parseUrl(String url) {
        boolean useSSL = false;
        int defaultPort = 80;

        String lowerUrl = url.toLowerCase();
        if (lowerUrl.startsWith("https://")) {
            useSSL = true;
            defaultPort = 443;
            url = url.substring(8); // strip off "https://"
        } else if (lowerUrl.startsWith("http://")) {
            url = url.substring(7); // strip off "http://"
        }

        String host;
        String path = "/";

        int slashIndex = url.indexOf('/');
        if (slashIndex >= 0) {
            host = url.substring(0, slashIndex);
            path = url.substring(slashIndex);
        } else {
            host = url;
        }

        int port = defaultPort;
        int colonIndex = host.indexOf(':');
        if (colonIndex >= 0) {
            port = Integer.parseInt(host.substring(colonIndex + 1));
            host = host.substring(0, colonIndex);
        }

        return new ParsedUrl(useSSL, host, port, path);
    }
}
