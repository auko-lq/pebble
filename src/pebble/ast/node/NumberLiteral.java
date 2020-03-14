package pebble.ast.node;

import pebble.ast.Token;

public class NumberLiteral extends ASTLeaf {
    public NumberLiteral(Token t) { super(t); }
    public int value() { return getToken().getNumber(); }
}
