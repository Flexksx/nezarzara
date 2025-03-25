package com.flexksx.url;

public class ParsedUrl {
    private final boolean useSSL;
    private final String host;
    private final int port;
    private final String path;

    public ParsedUrl(boolean useSSL, String host, int port, String path) {
        this.useSSL = useSSL;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ParsedUrl{" +
                "useSSL=" + useSSL +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                '}';
    }
}