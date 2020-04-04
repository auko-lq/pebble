package pebble.run.interpreter;


import pebble.ast.*;
import pebble.ast.node.ASTree;
import pebble.ast.node.NullStatement;
import pebble.ast.parser.BasicParser;
import pebble.exception.ParseException;
import pebble.exception.PebbleException;
import pebble.run.environment.BasicEnvironment;
import pebble.run.environment.Environment;
import pebble.run.evaluator.BasicEvaluator;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author: auko
 * @data 2020-03-13 0:30
 */
public class BasicInterpreter {
    private static String fileName = "E:\\project\\java\\pebble\\stone\\pebble\\src\\pebble\\test.pb";

    public static void main(String[] args) throws ParseException {
        run(new BasicParser(), new BasicEnvironment());
    }
    public static void run(BasicParser bp, Environment env)
            throws ParseException
    {
        Lexer lexer;
        try {
            lexer = new Lexer(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            throw new PebbleException("file not found");
        }
        System.out.println("pebble file read completed, ready to parse...");
        System.out.println("----------------------------------------------");
        while (lexer.hasNext()) {
            ASTree t = bp.parse(lexer);
            if (!(t instanceof NullStatement)) {
                Object r = ((BasicEvaluator.ASTreeExtend)t).eval(env);
//                if(t instanceof BasicEvaluator.NameExtend)
//                    System.out.println("=> " + r);
            }
        }
        System.out.println("\n----------------------------------------------");
    }
}
