package pebble;


import pebble.ast.*;
import pebble.ast.node.ASTree;
import pebble.ast.parser.BasicParser;
import pebble.exception.ParseException;
import stone.CodeDialog;

/**
 * @author: auko
 * @data 2020-03-11 15:23
 */
public class Test {
    public static void main(String[] args) throws ParseException {
        Lexer l = new Lexer(new CodeDialog());
        BasicParser bp = new BasicParser();
        while (l.hasNext()) {
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
    }
}
