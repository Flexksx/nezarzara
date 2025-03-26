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

    public static String fetch(String url) throws IOException {
        String cached = CacheService.get(url);
        if (cached != null) {
            return cached;
        }

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
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = factory.createSocket(parsed.getHost(), parsed.getPort());
        } else {
            socket = new Socket(parsed.getHost(), parsed.getPort());
        }

        OutputStream out = socket.getOutputStream();
        out.write(request.toString().getBytes());
        out.flush();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in = socket.getInputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        socket.close();

        String responseString = baos.toString();

        CacheService.put(url, responseString);
        CacheService.saveToFile();

        return responseString;
    }
}
