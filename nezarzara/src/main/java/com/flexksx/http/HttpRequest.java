package com.flexksx.http;

import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String host;
    private final String path;
    private final int port;
    private final Map<String, String> headers;

    HttpRequest(String method, String host, String path, int port, Map<String, String> headers) {
        this.method = method;
        this.host = host;
        this.path = path;
        this.port = port;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public int getPort() {
        return port;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(method).append(" ").append(path).append(" HTTP/1.1\r\n");
        sb.append("Host: ").append(host).append("\r\n");
        for (Map.Entry<String, String> e : headers.entrySet()) {
            sb.append(e.getKey()).append(": ").append(e.getValue()).append("\r\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }
}
