package pebble.run;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import pebble.ast.node.ASTree;
import pebble.ast.node.Fun;
import pebble.ast.node.Function;

import java.util.List;

@Require(FunctionEvaluator.class)
@Reviser
public class ClosureEvaluator {
    @Reviser
    public static class FunEx extends Fun {
        public FunEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            // 与def不同的是, def是创建好Function后put进环境中, 然后返回函数名
            // 而fun则是直接返回Function对象, 用于调用
            return new Function(parameters(), body(), env);
        }
    }
}
