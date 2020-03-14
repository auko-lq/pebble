package pebble.ast.node;

import pebble.ast.Token;

/**
 * @author: auko
 * @data 2020-03-11 21:16
 */
public class Name extends ASTLeaf{
    public Name(Token token){super(token);}
    public String getName(){return getToken().getText();}
}
