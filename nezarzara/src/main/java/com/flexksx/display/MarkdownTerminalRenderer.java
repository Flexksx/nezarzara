package com.flexksx.display;

import org.fusesource.jansi.AnsiConsole;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

public class MarkdownTerminalRenderer {
    public static void render(String markdown) {
        AnsiConsole.systemInstall();

        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);

        String plainText = document.getChars().toString();
        System.out.println(plainText);

        AnsiConsole.systemUninstall();
    }

}
