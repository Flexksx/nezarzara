package com.flexksx.cache;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CacheService {

    private static final String CACHE_FILE = "cache.ser";
    private static final long CACHE_EXPIRY_MS = TimeUnit.MINUTES.toMillis(5);

    private static final Map<String, CacheEntry> CACHE_MAP = new ConcurrentHashMap<>();

    static {
        loadFromFile();
    }

    public static String get(String url) {
        CacheEntry entry = CACHE_MAP.get(url);
        if (entry == null || entry.isExpired()) {
            return null;
        }
        System.out.println("CACHE HIT for " + url);
        return entry.getBody();
    }

    public static void put(String url, String body) {
        long expiryTime = System.currentTimeMillis() + CACHE_EXPIRY_MS;
        CACHE_MAP.put(url, new CacheEntry(body, expiryTime));
    }

    public static void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CACHE_FILE))) {
            out.writeObject(CACHE_MAP);
            System.out.println("Cache saved with " + CACHE_MAP.size() + " entries.");
        } catch (Exception e) {
            System.err.println("Error saving cache: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadFromFile() {
        File file = new File(CACHE_FILE);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof Map) {
                Map<String, CacheEntry> loaded = (Map<String, CacheEntry>) obj;
                loaded.entrySet().removeIf(e -> e.getValue().isExpired());
                CACHE_MAP.putAll(loaded);
                System.out.println("Cache loaded from file with " + CACHE_MAP.size() + " valid entries.");
            }
        } catch (Exception e) {
            System.err.println("Error loading cache: " + e.getMessage());
        }
    }

}
