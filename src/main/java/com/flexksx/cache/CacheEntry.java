package com.flexksx.cache;

import java.io.Serializable;

public class CacheEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String body;
    private final long expiryTime;

    public CacheEntry(String body, long expiryTime) {
        this.body = body;
        this.expiryTime = expiryTime;
    }

    public String getBody() {
        return body;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}