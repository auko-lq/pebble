package pebble.ast.node;

import pebble.ast.Token;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author: auko
 * @data 2020-03-11 21:12
 */
public class ASTLeaf extends ASTree{

    private static ArrayList<ASTree> empty = new ArrayList<>(1);
    private Token token;

    public ASTLeaf(Token token){
        this.token = token;
    }

    @Override
    public ASTree child(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int numChildren() {
        return 0;
    }

    @Override
    public Iterator<ASTree> children() {
        return empty.iterator();
    }

    @Override
    public String toString(){
        return token.getText();
    }

    public Token getToken(){
        return token;
    }

    @Override
    public String location() {
        return "at line " +token.getLineNumber();
    }
}
