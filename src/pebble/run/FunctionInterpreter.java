package pebble.run;


import pebble.ast.parser.FunctionParser;
import pebble.exception.ParseException;

public class FunctionInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new FunctionParser(), new NestedEnvironment());
    }
}
