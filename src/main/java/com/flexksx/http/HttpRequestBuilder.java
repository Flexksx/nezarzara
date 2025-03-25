package com.flexksx.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {
    private String method = "GET";
    private String host = "";
    private String path = "/";
    private int port = 80;
    private Map<String, String> headers = new HashMap<>();

    public HttpRequestBuilder setMethod(String method) {
        this.method = method;
        return this;
    }

    public HttpRequestBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public HttpRequestBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public HttpRequestBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public HttpRequestBuilder addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpRequest build() {
        return new HttpRequest(method, host, path, port, headers);
    }
}
