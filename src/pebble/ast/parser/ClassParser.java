package pebble.ast.parser;

import pebble.ast.Token;
import pebble.ast.node.ClassBody;
import pebble.ast.node.ClassStatement;
import pebble.ast.node.Dot;

import static pebble.ast.parser.Parser.rule;

public class ClassParser extends ClosureParser {
    // 在member中添加自己定义的var语法规则
    Parser member = rule().or(var, def, simple);
    Parser class_body = rule(ClassBody.class).sep("{").option(member)
                            .repeat(rule().sep(";", Token.EOL).option(member))
                            .sep("}");
    Parser defclass = rule(ClassStatement.class).sep("class").identifier(reserved)
                          .option(rule().sep("extends").identifier(reserved))
                          .ast(class_body);
    public ClassParser() {
        postfix.insertChoice(rule(Dot.class).sep(".").identifier(reserved));
        program.insertChoice(defclass);
    }
}
