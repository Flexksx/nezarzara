package com.flexksx;

import java.net.http.HttpRequest;

import com.flexksx.commands.Command;
import com.flexksx.commands.CommandFactory;
import com.flexksx.commands.HelpCommand;
import com.flexksx.http.HttpRequester;

public class Nezarzara {
    public static void main(String[] args) {

        if (args.length == 0) {
            new HelpCommand().run(args);
            return;
        }

        String option = args[0];

        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);

        Command command = CommandFactory.getCommand(option);
        command.run(subArgs);
    }
}
