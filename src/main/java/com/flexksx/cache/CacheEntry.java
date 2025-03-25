package com.flexksx.cache;

import java.io.Serializable;
import java.net.http.HttpResponse;

public class CacheEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private final HttpResponse response;
    private final long expiryTime;

    public CacheEntry(HttpResponse response, long expiryTime) {
        this.response = response;
        this.expiryTime = expiryTime;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
