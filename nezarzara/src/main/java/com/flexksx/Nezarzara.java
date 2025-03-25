package com.flexksx;

import com.flexksx.domain.SearchByUriUseCase;
import com.flexksx.http.HttpRequester;

/**
 * Hello world!
 *
 */
public class Nezarzara {
    public static void main(String[] args) {
        String uri = "https://999.md/ro";
        System.out.println(SearchByUriUseCase.execute(uri));
    }
}
