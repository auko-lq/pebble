package pebble.exception;


import pebble.ast.Token;

import java.io.IOException;

/**
 * @author: auko
 * @data 2020-03-11 12:01
 */
public class ParseException extends Exception {

    public ParseException(IOException e) {
        super(e);
    }

    public ParseException(String msg) {
        super(msg);
    }

    protected static String location(Token token) {
        if (token == Token.EOF) {
            return "the last line";
        } else {
            return String.format("\"%s\" at line %d", token.getText(), token.getLineNumber());
        }
    }
}
