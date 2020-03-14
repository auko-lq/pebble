package pebble.run;


import pebble.ast.parser.ClosureParser;
import pebble.exception.ParseException;

public class ClosureInterpreter extends BasicInterpreter{
    public static void main(String[] args) throws ParseException {
        run(new ClosureParser(), new NestedEnvironment());
    }
}
