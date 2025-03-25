
package com.flexksx.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CacheManager {
    private static final String CACHE_FILE = "go2web_cache.ser";
    private static final long EXPIRY_MS = TimeUnit.MINUTES.toMillis(5);

    private Map<String, CacheEntry> cacheMap = new ConcurrentHashMap<>();

    public void loadFromFile() {
        File file = new File(CACHE_FILE);
        if (!file.exists())
            return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            Map<String, CacheEntry> loaded = (Map<String, CacheEntry>) in.readObject();
            // Remove expired
            loaded.entrySet().removeIf(e -> e.getValue().isExpired());
            this.cacheMap = loaded;
            System.out.println("Cache loaded from file with " + cacheMap.size() + " valid entries.");
        } catch (Exception e) {
            System.err.println("Error loading cache: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CACHE_FILE))) {
            out.writeObject(cacheMap);
            System.out.println("Cache saved to file with " + cacheMap.size() + " entries.");
        } catch (Exception e) {
            System.err.println("Error saving cache: " + e.getMessage());
        }
    }

    public HttpResponse get(String url) {
        CacheEntry entry = cacheMap.get(url);
        if (entry == null)
            return null;
        if (entry.isExpired()) {
            cacheMap.remove(url);
            return null;
        }
        System.out.println("CACHE HIT FOUND AND LOADED!");
        return entry.getResponse();
    }

    public void put(String url, HttpResponse response) {
        long expiryTime = System.currentTimeMillis() + EXPIRY_MS;
        cacheMap.put(url, new CacheEntry(response, expiryTime));
    }
}
