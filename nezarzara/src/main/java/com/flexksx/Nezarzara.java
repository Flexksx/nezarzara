package com.flexksx;

import com.flexksx.domain.SearchByUriAndPrintContentUseCase;

/**
 * Hello world!
 *
 */
public class Nezarzara {
    public static void main(String[] args) {
        String uri = "https://999.md/ro";
        SearchByUriAndPrintContentUseCase.execute(uri);
    }
}
