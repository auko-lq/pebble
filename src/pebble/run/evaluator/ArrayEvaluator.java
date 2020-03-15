package pebble.run.evaluator;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import pebble.ast.node.ASTree;
import pebble.ast.node.ArrayLiteral;
import pebble.ast.node.ArrayReference;
import pebble.ast.node.PrimaryExpression;
import pebble.ast.parser.ArrayParser;
import pebble.exception.PebbleException;
import pebble.exception.SyntaxException;
import pebble.run.environment.Environment;
import pebble.run.evaluator.BasicEvaluator.ASTreeExtend;
import pebble.run.evaluator.BasicEvaluator.BinaryExtend;
import pebble.run.evaluator.FunctionEvaluator.PrimaryExtend;

import java.util.List;

@Require({FunctionEvaluator.class, ArrayParser.class})
@Reviser public class ArrayEvaluator {
    @Reviser public static class ArrayLiteralExtend extends ArrayLiteral {
        public ArrayLiteralExtend(List<ASTree> list) { super(list); }
        public Object eval(Environment env) {
            int s = numChildren();
            Object[] res = new Object[s];
            int i = 0;
            // 初始化数组
            for (ASTree t: this) {
                res[i++] = ((ASTreeExtend)t).eval(env);
            }
            return res;
        }
    }
    @Reviser public static class ArrayRefExtend extends ArrayReference {
        public ArrayRefExtend(List<ASTree> c) { super(c); }
        public Object eval(Environment env, Object value) {
            if (value instanceof Object[]) {
                Object index = ((ASTreeExtend)index()).eval(env);
                if (index instanceof Integer)
                    return ((Object[])value)[(Integer)index];
            }

            throw new PebbleException("bad array access", this);
        }
    }
    @Reviser public static class AssignExtend extends BinaryExtend {
        public AssignExtend(List<ASTree> c) { super(c); }
        @Override
        protected Object computeAssign(Environment env, Object rvalue) throws SyntaxException {
            ASTree le = left();
            if (le instanceof PrimaryExpression) {
                PrimaryExtend p = (PrimaryExtend) le;
                if (p.hasPostfix(0) && p.postfix(0) instanceof ArrayReference) {
                    Object a = ((PrimaryExtend)le).evalSubExpression(env, 1);
                    if (a instanceof Object[]) {
                        // 计算下标值
                        ArrayReference aref = (ArrayReference) p.postfix(0);
                        Object index = ((ASTreeExtend)aref.index()).eval(env);
                        if (index instanceof Integer) {
                            ((Object[])a)[(Integer)index] = rvalue;
                            return rvalue;
                        }
                    }
                    throw new PebbleException("bad array access", this);
                }
            }
            return super.computeAssign(env, rvalue);
        }
    }       
}
