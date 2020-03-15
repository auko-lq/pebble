package pebble.run.interpreter;


import pebble.ast.parser.ClosureParser;
import pebble.exception.ParseException;
import pebble.run.environment.NestedEnvironment;

public class ClosureInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new ClosureParser(), new NestedEnvironment());
    }
}
