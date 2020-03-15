package pebble.ast.parser;

import javassist.gluonj.Reviser;
import pebble.ast.node.ArrayLiteral;
import pebble.ast.node.ArrayReference;

import static pebble.ast.parser.Parser.rule;


// 注意, 这里不是用继承的方式, 而是用修改器的方式
@Reviser public class ArrayParser extends FunctionParser {
    Parser elements = rule(ArrayLiteral.class)
                          .ast(expr).repeat(rule().sep(",").ast(expr));
    public ArrayParser() {
        reserved.add("]");
        primary.insertChoice(rule().sep("[").maybe(elements).sep("]"));
        postfix.insertChoice(rule(ArrayReference.class).sep("[").ast(expr).sep("]"));
    }
}
