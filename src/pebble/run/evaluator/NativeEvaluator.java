package pebble.run.evaluator;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import pebble.ast.node.ASTree;
import pebble.ast.integration.NativeFunction;
import pebble.exception.PebbleException;
import pebble.run.environment.Environment;
import pebble.run.evaluator.BasicEvaluator.ASTreeExtend;
import pebble.run.evaluator.FunctionEvaluator.ArgumentsExtend;

import java.util.List;

@Require(FunctionEvaluator.class)
@Reviser public class NativeEvaluator {

    // 此修改器继承自ArgumentsExtend, 但修改的依然是Arguments
    @Reviser public static class NativeArgExtend extends ArgumentsExtend {
        public NativeArgExtend(List<ASTree> c) { super(c); }
        @Override public Object eval(Environment callerEnv, Object value) {
            if (!(value instanceof NativeFunction))
                return super.eval(callerEnv, value);

            NativeFunction func = (NativeFunction)value;
            int nparams = func.numOfParameters();
            if (size() != nparams)
                throw new PebbleException("bad number of arguments", this);

            // 包装参数, 准备调用
            Object[] args = new Object[nparams];
            int num = 0;
            for (ASTree a: this) {
                ASTreeExtend ae = (ASTreeExtend) a;
                args[num++] = ae.eval(callerEnv);
            }
            return func.invoke(args, this);
        }
    }
}
