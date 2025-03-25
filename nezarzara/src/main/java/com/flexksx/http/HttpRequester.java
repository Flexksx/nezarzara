package com.flexksx.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

import com.flexksx.url.ParsedUrl;
import com.flexksx.url.UrlParser;

public class HttpRequester {
    public static String fetch(String url) throws IOException {
        ParsedUrl parsedUrl = UrlParser.parseUrl(url);

        HttpRequest request = new HttpRequestBuilder()
                .setMethod("GET")
                .setHost(parsedUrl.getHost())
                .setPath(parsedUrl.getPath())
                .setPort(parsedUrl.getPort())
                .addHeader("Accept", "text/html, application/json")
                .addHeader("Connection", "close")
                .build();

        Socket socket;
        if (parsedUrl.isUseSSL()) {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = factory.createSocket(parsedUrl.getHost(), parsedUrl.getPort());
        } else {
            socket = new Socket(parsedUrl.getHost(), parsedUrl.getPort());
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

        return new String(baos.toByteArray());
    }
}
