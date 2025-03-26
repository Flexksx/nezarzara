package com.flexksx.http;

import com.flexksx.cache.CacheService;
import com.flexksx.url.ParsedUrl;
import com.flexksx.url.UrlParser;

import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpRequester {

    private static final int MAX_REDIRECTS = 5;

    public static String fetch(String url) throws IOException {
        return fetch(url, 0);
    }

    private static String fetch(String url, int redirectCount) throws IOException {
        if (redirectCount > MAX_REDIRECTS)
            throw new IOException("Too many redirects");
        String cached = CacheService.get(url);
        if (cached != null)
            return cached;
        ParsedUrl parsed = UrlParser.parseUrl(url);

        HttpRequest request = new HttpRequestBuilder()
                .setMethod("GET")
                .setHost(parsed.getHost())
                .setPath(parsed.getPath())
                .setPort(parsed.getPort())
                .addHeader("Accept", "text/html, application/json")
                .addHeader("Connection", "close")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build();

        Socket socket;
        if (parsed.isUseSSL()) {
            SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = f.createSocket(parsed.getHost(), parsed.getPort());
        } else {
            socket = new Socket(parsed.getHost(), parsed.getPort());
        }

        OutputStream out = socket.getOutputStream();
        out.write(request.toString().getBytes());
        out.flush();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in = socket.getInputStream();
        byte[] buffer = new byte[4096];
        int r;
        while ((r = in.read(buffer)) != -1) {
            baos.write(buffer, 0, r);
        }
        socket.close();
        String resp = baos.toString();
        String redirectUrl = checkRedirect(resp, parsed, redirectCount);
        if (redirectUrl != null)
            return redirectUrl;
        CacheService.put(url, resp);
        CacheService.saveToFile();
        return resp;
    }

    private static String checkRedirect(String response, ParsedUrl parsed, int redirectCount) throws IOException {
        int idx = response.indexOf("\r\n\r\n");
        if (idx < 0)
            idx = response.indexOf("\n\n");
        String h = idx < 0 ? response : response.substring(0, idx);
        String[] lines = h.split("\\r?\\n", -1);
        if (lines.length > 0 && lines[0].startsWith("HTTP/")) {
            String[] parts = lines[0].split(" ", 3);
            if (parts.length > 1) {
                int code = Integer.parseInt(parts[1]);
                if (code >= 300 && code < 400) {
                    String loc = null;
                    for (String line : lines) {
                        if (line.toLowerCase().startsWith("location:")) {
                            loc = line.substring(line.indexOf(':') + 1).trim();
                            break;
                        }
                    }
                    if (loc != null) {
                        if (!loc.startsWith("http")) {
                            String p = parsed.isUseSSL() ? "https://" : "http://";
                            loc = p + parsed.getHost() + (loc.startsWith("/") ? "" : "/") + loc;
                        }

                        System.out.println("Redirecting to " + loc);
                        return fetch(loc, redirectCount + 1);
                    }
                }
            }
        }
        return null;
    }
}
