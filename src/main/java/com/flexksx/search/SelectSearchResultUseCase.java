package com.flexksx.search;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.flexksx.fetch.FetchByUriUseCase;
import com.flexksx.http.HttpRequester;

/**
 * Use case 2: Display a list of results, prompt the user to select one,
 * then fetch and display that page's content.
 */
public class SelectSearchResultUseCase {

    public void execute(List<SearchResult> results) {
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        // Display them
        for (int i = 0; i < results.size(); i++) {
            // Only show top 10 for brevity
            if (i == 10)
                break;

            SearchResult r = results.get(i);
            System.out.println((i + 1) + ". " + r.getTitle());
            System.out.println("   " + r.getUrl());
            if (!r.getSnippet().isEmpty()) {
                System.out.println("   " + r.getSnippet());
            }
            System.out.println();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a number (1-10) to open a result, or 'q' to quit:");
        String input = scanner.nextLine().trim();
        if (!input.equalsIgnoreCase("q")) {
            try {
                int choice = Integer.parseInt(input);
                int maxShown = Math.min(10, results.size());
                if (choice >= 1 && choice <= maxShown) {
                    SearchResult selected = results.get(choice - 1);
                    String url = selected.getUrl();

                    System.out.println("\n=== PAGE CONTENT (truncated) ===");
                    FetchByUriUseCase.execute(url);

                } else {
                    System.out.println("Invalid choice: " + input);
                }
                scanner.close();
            } catch (NumberFormatException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
