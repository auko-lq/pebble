package pebble.run.interpreter;


import pebble.ast.integration.Natives;
import pebble.ast.parser.ClosureParser;
import pebble.exception.ParseException;
import pebble.run.environment.NestedEnvironment;

public class NativeInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new ClosureParser(),
            new Natives().environment(new NestedEnvironment()));
    }
}
