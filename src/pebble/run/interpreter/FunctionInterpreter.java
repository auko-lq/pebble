package pebble.run.interpreter;


import pebble.ast.parser.FunctionParser;
import pebble.exception.ParseException;
import pebble.run.environment.NestedEnvironment;

public class FunctionInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new FunctionParser(), new NestedEnvironment());
    }
}
