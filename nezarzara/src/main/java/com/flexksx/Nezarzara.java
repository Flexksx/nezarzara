package com.flexksx;

import com.flexksx.domain.FetchByUriUseCase;
import com.flexksx.domain.SearchByQueryUseCase;

/**
 * Hello world!
 *
 */
public class Nezarzara {
    public static void main(String[] args) {
        String query = "parrots";
        // FetchByUriUseCase.execute(uri);
        SearchByQueryUseCase.execute(query);
    }
}
