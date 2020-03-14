package pebble.ast;

import pebble.exception.PebbleException;

/**
 * @author: auko
 * @data 2020-03-11 0:35
 */
public abstract class Token {
    public static final Token EOF = new Token(-1, TokenType.EOF) {};
    public static final String EOL = "\\n";

    private int lineNumber;
    private String type;

    protected Token(int line, String type) {
        this.lineNumber = line;
        this.type = type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return "";
    }

    public int getNumber() {
        throw new PebbleException("not number token");
    }
}
