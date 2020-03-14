package pebble.ast;

import pebble.exception.ParseException;
import stone.CodeDialog;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: auko
 * @data 2020-03-11 12:00
 */
public class Lexer {

    public static void main(String[] args) throws ParseException {
        Lexer l = new Lexer(new CodeDialog());
        for (Token token; l.hasNext(); ) {
            token = l.next();
            System.out.println("=> " + token.getText() + "  " + token.getType());
        }
    }

    public static String regexPat = "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
            + "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    private Pattern pattern = Pattern.compile(regexPat);

    private ArrayList<Token> queue = new ArrayList<>();
    private LineNumberReader reader;
    private boolean hasMore;

    public Lexer(Reader r) {
        hasMore = true;
        reader = new LineNumberReader(r);
    }

    public boolean hasNext() throws ParseException {
        return peek(0) != Token.EOF;
    }

    public Token next() throws ParseException {
        // 查看queue头有没有内容
        if (fillQueue(0)) {
            return queue.remove(0);
        } else {
            return Token.EOF;
        }
    }

    public Token peek(int i) throws ParseException {
        // 先看看queue够不够peek到i
        if (fillQueue(i)) {
            return queue.get(i);
        } else {
            return Token.EOF;
        }
    }

    private boolean fillQueue(int i) throws ParseException {
        // 检查queue够不够get到i, 不够就去read
        while (i >= queue.size()) {
            if (hasMore) {
                readLine();
            } else {
                return false;
            }
        }
        return true;
    }


    private void readLine() throws ParseException {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new ParseException(e);
        }
        if (line == null) {
            hasMore = false;
            return;
        }

        int lineNum = reader.getLineNumber();
        Matcher matcher = pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int index = 0;
        int endIndex = line.length();
        while (index < endIndex) {
            matcher.region(index, endIndex);
            // 能否匹配
            if (matcher.lookingAt()) {
                tokenToQueue(lineNum, matcher);
                index = matcher.end();
            } else {
                throw new ParseException("bad token at line " + lineNum);
            }
        }

        // 读完一行后, 加个EOL
        queue.add(new IdentifierToken(lineNum, Token.EOL));
    }


    /**
     * 处理一个token放入queue
     *
     * @param lineNum 当前行号
     * @param matcher
     */
    private void tokenToQueue(int lineNum, Matcher matcher) {
        // \\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?
        // 匹配第一个组, 即除空格外所有情况
        String m = matcher.group(1);
        if (m != null) {
            // 非空格
            if (matcher.group(2) == null) {
                // 非注释
                Token token;
                if (matcher.group(3) != null) {
                    token = new NumberToken(lineNum, Integer.parseInt(m));
                } else if (matcher.group(4) != null) {
                    token = new StringToken(lineNum, toStringLiteral(m));
                } else {
                    token = new IdentifierToken(lineNum, m);
                }
                queue.add(token);
            }
        }
    }

    /**
     * 处理字符串中的转义符 \
     *
     * @param s 待处理字符串
     * @return 处理后字符串
     */
    private String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length() - 1;
        for (int i = 1; i < len; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < len) {
                int c2 = s.charAt(i + 1);
                if (c2 == '"' || c2 == '\\') {
                    c = s.charAt(++i);
                } else if (c2 == 'n') {
                    ++i;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }


    protected static class IdentifierToken extends Token {
        private String text;

        protected IdentifierToken(int line, String identifier) {
            super(line, TokenType.IDENTIFIER);
            text = identifier;
        }

        public String getText() {
            return text;
        }
    }

    protected static class NumberToken extends Token {
        private int value;

        protected NumberToken(int line, int value) {
            super(line, TokenType.NUMBER);
            this.value = value;
        }

        public String getText() {
            return Integer.toString(value);
        }

        public int getNumber() {
            return value;
        }
    }

    protected static class StringToken extends Token {
        private String literal;

        protected StringToken(int line, String string) {
            super(line, TokenType.STRING);
            literal = string;
        }

        public String getText() {
            return literal;
        }
    }
}
