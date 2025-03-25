package com.flexksx.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private static final Map<String, Command> COMMANDS = new HashMap<>();
    static {
        COMMANDS.put("-h", new HelpCommand());
        COMMANDS.put("--help", new HelpCommand());
        COMMANDS.put("-s", new SearchCommand());
        COMMANDS.put("-u", new UrlCommand());
    }

    public static Command getCommand(String option) {
        // fallback to help if we don't recognize the option
        return COMMANDS.getOrDefault(option, new HelpCommand());
    }
}
