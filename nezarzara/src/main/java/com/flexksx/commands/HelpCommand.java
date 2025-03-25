package com.flexksx.commands;

public class HelpCommand implements Command {
    @Override
    public void run(String[] args) {
        String message = new StringBuilder()
                .append("Usage:\n")
                .append("  go2web -h               Show this help\n")
                .append("  go2web -s <search-term> Search the web\n")
                .append("  go2web -u <URL>         Fetch a URL in a readable format\n")
                .toString();
        System.out.println(message);
    }
}
