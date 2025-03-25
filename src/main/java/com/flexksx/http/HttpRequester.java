package com.flexksx.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import com.flexksx.url.ParsedUrl;
import com.flexksx.url.UrlParser;

/**
 * Demonstrates in-memory caching with file persistence in HttpRequester.
 */
public class HttpRequester {

    private static final String CACHE_FILE = "go2web_cache.ser";

    private static Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private static final long CACHE_EXPIRY_MS = TimeUnit.MINUTES.toMillis(5);

    /**
     * Fetches content from the given URL, checking the on-disk/in-memory cache
     * first.
     * 
     * @param url the resource location (e.g. "https://example.com")
     * @return the raw HTTP response (headers + body)
     * @throws IOException if there's a network or socket error
     */
    public static String fetch(String url) throws IOException {
        CacheEntry entry = cache.get(url);
        if (entry != null && !entry.isExpired()) {
            System.out.println("CACHE HIT for " + url);
            return entry.getBody();
        }

        ParsedUrl parsedUrl = UrlParser.parseUrl(url);

        HttpRequest request = new HttpRequestBuilder()
                .setMethod("GET")
                .setHost(parsedUrl.getHost())
                .setPath(parsedUrl.getPath())
                .setPort(parsedUrl.getPort())
                .addHeader("Accept", "text/html, application/json")
                .addHeader("Connection", "close")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
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

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream in = socket.getInputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, read);
        }
        socket.close();

        String responseString = byteArrayOutputStream.toString();

        cache.put(url, new CacheEntry(responseString, System.currentTimeMillis() + CACHE_EXPIRY_MS));

        return responseString;
    }

    @SuppressWarnings("unchecked")
    public static void loadCacheFromFile() {
        File file = new File(CACHE_FILE);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                Map<String, CacheEntry> loaded = (Map<String, CacheEntry>) obj;
                // remove expired
                loaded.entrySet().removeIf(e -> e.getValue().isExpired());
                cache = new ConcurrentHashMap<>(loaded);
                System.out.println("Cache loaded from file with " + cache.size() + " valid entries.");
            }
        } catch (Exception e) {
            System.err.println("Error loading cache file: " + e.getMessage());
        }
    }

    /**
     * Saves the current cache to a file on disk.
     * This should be called once at program exit.
     */
    public static void saveCacheToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CACHE_FILE))) {
            oos.writeObject(cache);
            System.out.println("Cache saved to file with " + cache.size() + " entries.");
        } catch (Exception e) {
            System.err.println("Error saving cache file: " + e.getMessage());
        }
    }

    /**
     * Represents a cached response (the entire raw body)
     * plus an expiry time.
     */
    private static class CacheEntry implements Serializable {
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
}
