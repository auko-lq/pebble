package pebble.ast.parser;


import pebble.ast.node.Fun;

import static pebble.ast.parser.Parser.rule;


public class ClosureParser extends FunctionParser {
    public ClosureParser() {
        primary.insertChoice(rule(Fun.class)
                                 .sep("fun").ast(paramList).ast(block));
    }
}
