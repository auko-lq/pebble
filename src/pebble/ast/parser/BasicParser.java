package pebble.ast.parser;

import pebble.ast.*;
import pebble.ast.node.*;
import pebble.ast.parser.Parser.Operators;
import pebble.exception.ParseException;

import static pebble.ast.parser.Parser.rule;

import java.util.HashSet;

public class BasicParser {

    // 这里面的是无法作为变量名使用的标识符
    HashSet<String> reserved = new HashSet<>();


    Operators operators = new Operators();
    Parser expr0 = rule();

    // rule(class) 里面的class是用来标志生成的AST树的节点类型
    // 还有个作用就是通过添加类属性, 防止被折叠, 比如ASTList和NegativeExpression都只有一个子节点
    // ASTList会被折叠, 而NegativeExpression不会, 这是信息量的区别
    Parser primary = rule(PrimaryExpression.class)
            .or(rule().sep("(").ast(expr0).sep(")"),
                    rule().number(NumberLiteral.class),
                    rule().identifier(Name.class, reserved),
                    rule().string(StringLiteral.class));

    Parser factor = rule().or(rule(NegativeExpression.class).sep("-").ast(primary),
            primary);
    Parser expr = expr0.expression(BinaryExpression.class, factor, operators);

    Parser var = rule(VarExpression.class).sep("var")
            .repeat(rule().identifier(Name.class, reserved)
                    .option(rule().sep("=").ast(expr)));

    Parser statement0 = rule();
    Parser block = rule(BlockStatement.class)
            .sep("{").option(statement0)
            .repeat(rule().sep(";", Token.EOL).option(statement0))
            .sep("}");
    Parser simple = rule(PrimaryExpression.class).ast(expr);

    Parser statement = statement0.or(
            rule(IfStatement.class).sep("if").ast(expr).ast(block)
                    .repeat(rule().sep("elif").ast(expr).ast(block))
                    .option(rule().sep("else").ast(block)),
            rule(WhileStatement.class).sep("while").ast(expr).ast(block),
            var,
            simple
    );

    // 这里使用了空对象模式
    // 按照语法规则 program: [statement] (";" | EOL) , statement部分用option方法更好
    // 但是为了特别地表示statement不存在的情况, 即"空", 语法规则改造成 program: (statement | 空) (";" | EOL)
    // 于是改成了or方法, 且为了避免"空"情况被AST折叠, 特意定义了一个NullStatement类
    Parser program = rule().or(statement, rule(NullStatement.class))
            .sep(";", Token.EOL);

    public BasicParser() {
        reserved.add(";");
        reserved.add("}");
        reserved.add(Token.EOL);

        operators.add("=", 1, Operators.RIGHT);
        operators.add("==", 2, Operators.LEFT);
        operators.add(">", 2, Operators.LEFT);
        operators.add("<", 2, Operators.LEFT);
        operators.add("+", 3, Operators.LEFT);
        operators.add("-", 3, Operators.LEFT);
        operators.add("*", 4, Operators.LEFT);
        operators.add("/", 4, Operators.LEFT);
        operators.add("%", 4, Operators.LEFT);
    }

    public ASTree parse(Lexer lexer) throws ParseException {
        return program.parse(lexer);
    }
}
