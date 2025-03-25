package com.flexksx;

import com.flexksx.commands.Command;
import com.flexksx.commands.CommandFactory;
import com.flexksx.commands.HelpCommand;

public class Nezarzara {
    public static void main(String[] args) {
        if (args.length == 0) {
            // no args => help
            new HelpCommand().run(args);
            return;
        }

        // The first arg is the option
        String option = args[0];

        // The rest are sub-args
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);

        Command command = CommandFactory.getCommand(option);
        command.run(subArgs);
    }
}
