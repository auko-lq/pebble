package pebble.ast.node;

import pebble.ast.Token;

public class StringLiteral extends ASTLeaf {
    public StringLiteral(Token t) { super(t); }
    public String value() { return getToken().getText(); }
}
