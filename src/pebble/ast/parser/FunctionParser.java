package pebble.ast.parser;

import pebble.ast.node.Arguments;
import pebble.ast.node.DefStatement;
import pebble.ast.node.ParameterList;

import static pebble.ast.parser.Parser.rule;


/**
 * @author: auko
 * @data 2020-03-13 13:47
 */
public class FunctionParser extends BasicParser {
    Parser param = rule().identifier(reserved);
    Parser params = rule(ParameterList.class)
            .ast(param).repeat(rule().sep(",").ast(param));

    // maybe和option 的区别在于maybe即使被忽略也会创建该节点(形成单一节点的树)
    // 作用在于生成的节点会被折叠而直接表示paramList
    Parser paramList = rule().sep("(").maybe(params).sep(")");
    Parser def = rule(DefStatement.class)
            .sep("def").identifier(reserved).ast(paramList).ast(block);
    Parser args = rule(Arguments.class)
            .ast(expr).repeat(rule().sep(",").ast(expr));
    Parser postfix = rule().sep("(").maybe(args).sep(")");


    public FunctionParser() {
        reserved.add(")");
        primary.repeat(postfix);
        simple.option(args);
        program.insertChoice(def);
    }
}
