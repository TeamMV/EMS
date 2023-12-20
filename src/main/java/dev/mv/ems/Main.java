package dev.mv.ems;

import dev.mv.ems.parser.Parser;
import dev.mv.ems.parser.ast.Statement;
import dev.mv.ems.parser.lexer.Lexer;
import dev.mv.ems.parser.lexer.Token;
import dev.mv.engine.ApplicationConfig;
import dev.mv.engine.MVEngine;
import dev.mv.engine.render.WindowCreateInfo;
import dev.mv.engine.render.shared.Window;
import dev.mv.utils.misc.Version;

public class Main {
    public static void main(String[] args) {
        //TODO: features
        //Context: contains all known variables, their starting values, and inferred types.
        //Use context to infer type of ident expression
        //Update type when using "assign" to a variable
        //Have a private list of variables and their types, update while parsing
        //Use the list to find variables that aren't created, and to type check
        //Function calls and if statements at compile time, before any code is executed
        Parser parser = new Parser("""
                x=10y=sin(x)plus=(10+x)minus=plus+plus divide=plus-minus/divide+plus
                if y < x || x > 20 do
                    x += 0.01
                    y -= x / 100 ** 2
                    if x <> y && x != y and not (x == y) {
                        x >>>= 2
                    end
                }
                x += 0.01
                """);

        for (Statement stmt : parser.parse()) {
            System.out.print(stmt);
        }

        System.exit(0);

        try (MVEngine engine = MVEngine.init(new ApplicationConfig()
                .setDimension(ApplicationConfig.GameDimension.ONLY_2D)
                .setName("EMS")
                .setVersion(Version.parse("1.0.0"))
        )) {
            WindowCreateInfo info = new WindowCreateInfo();
            info.title = "Easy Model Simulation";
            info.decorated = true;
            info.vsync = true;
            info.width = 800;
            info.height = 600;
            info.resizeable = true;

            Window window = engine.createWindow(info);
            window.run(new Loop());
        }
    }
}
