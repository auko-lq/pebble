package pebble.exception;


import pebble.ast.Token;
import pebble.ast.node.ASTree;
import pebble.run.FunctionEvaluator;

/**
 * @author: auko
 * @data 2020-03-12 23:43
 */
public class SyntaxException extends ParseException {
    public SyntaxException(Token token) {
        this("",token);
    }

    public SyntaxException(String msg, String location){
        super("syntax error around " + location + ". " + msg);
    }

    public SyntaxException(String msg, Token token) {
        super("syntax error around " + location(token) + ". " + msg);
    }

    public SyntaxException(String msg, ASTree tree) {
        super("syntax error around " + tree.location() + ". " + msg);
    }
}
