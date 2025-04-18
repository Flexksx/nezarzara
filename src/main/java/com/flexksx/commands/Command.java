package com.flexksx.commands;

public interface Command {
    /**
     * Runs the command, passing in the full CLI arguments.
     *
     * @param args the full array of arguments (beyond the option itself)
     */
    void run(String[] args);
}
