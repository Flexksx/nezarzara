
package com.flexksx.commands;

import java.util.List;

import com.flexksx.search.SearchByQueryUseCase;
import com.flexksx.search.SearchResult;
import com.flexksx.search.SelectSearchResultUseCase;

public class SearchCommand implements Command {

    @Override
    public void run(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: No search term provided. Usage: go2web -s <search-term>");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0)
                sb.append(" ");
            sb.append(args[i]);
        }
        String searchTerm = sb.toString();

        try {
            SearchByQueryUseCase searchUseCase = new SearchByQueryUseCase();
            List<SearchResult> results = searchUseCase.execute(searchTerm);

            SelectSearchResultUseCase selectUseCase = new SelectSearchResultUseCase();
            selectUseCase.execute(results);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
