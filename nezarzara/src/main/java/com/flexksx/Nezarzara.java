package com.flexksx;

import com.flexksx.http.HttpRequester;

/**
 * Hello world!
 *
 */
public class Nezarzara {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        String uri = "https://999.md/ro";
        try {
            String response = HttpRequester.fetch(uri);
            System.out.println(response);
        } catch (Exception e) {
            System.err.println("Error happened when trying to fetch " + uri);
        }
    }
}
