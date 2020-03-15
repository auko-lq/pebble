package pebble.run.interpreter;

import pebble.ast.integration.Natives;
import pebble.ast.parser.ClassParser;
import pebble.exception.ParseException;
import pebble.run.environment.NestedEnvironment;

public class ClassInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new ClassParser(), new Natives().environment(new NestedEnvironment()));
    }
}
