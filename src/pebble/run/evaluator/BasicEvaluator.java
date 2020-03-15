package pebble.run.evaluator;

import javassist.gluonj.Reviser;
import pebble.ast.*;
import pebble.ast.node.*;
import pebble.exception.PebbleException;
import pebble.exception.SyntaxException;
import pebble.run.environment.Environment;
import pebble.run.evaluator.FunctionEvaluator.EnvironmentExtend;

import java.util.List;

// Reviser注解的功能是: 将子类的方法替换到父类中
// 这样的好处是, 统一管理某一个功能, 比如此处的各节点eval方法, 不需要再去修改原来的类
@Reviser
public class BasicEvaluator {
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    // NaN为定义时的初始值
    public static final String NaN = "NaN";

    @Reviser
    public static abstract class ASTreeExtend extends ASTree {
        public abstract Object eval(Environment env);
    }

    @Reviser
    public static class ASTListExtend extends ASTList {
        public ASTListExtend(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            throw new PebbleException("cannot eval: " + toString(), this);
        }
    }

    @Reviser
    public static class ASTLeafExtend extends ASTLeaf {
        public ASTLeafExtend(Token t) {
            super(t);
        }

        public Object eval(Environment env) {
            throw new PebbleException("cannot eval: " + toString(), this);
        }
    }

    @Reviser
    public static class NumberExtend extends NumberLiteral {
        public NumberExtend(Token t) {
            super(t);
        }

        public Object eval(Environment e) {
            return value();
        }
    }

    @Reviser
    public static class StringExtend extends StringLiteral {
        public StringExtend(Token t) {
            super(t);
        }

        public Object eval(Environment e) {
            return value();
        }
    }

    @Reviser
    public static class NameExtend extends Name {
        public NameExtend(Token t) {
            super(t);
        }

        public Object eval(Environment env) throws SyntaxException {
            Object value = env.get(getName());
            if (value == null)
                throw new PebbleException("undefined name: " + getName(), this);
            else if(value == NaN)
                throw new SyntaxException(getName() + " is NaN",this);
                return value;
        }
    }

    @Reviser
    public static class NegativeExtend extends NegativeExpression {
        public NegativeExtend(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            // 这里的operand用得不恰当, 意思是操作数, 实际上获取的是子表达式
            Object v = ((ASTreeExtend) operand()).eval(env);
            if (v instanceof Integer)
                return new Integer(-((Integer) v).intValue());
            else
                throw new PebbleException("bad type for -", this);
        }
    }

    @Reviser
    public static class BinaryExtend extends BinaryExpression {
        public BinaryExtend(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) throws SyntaxException {
            String op = getOperator();
            if ("=".equals(op)) {
                // 计算右值后向左赋值
                Object right = ((ASTreeExtend) right()).eval(env);
                return computeAssign(env, right);
            } else {
                // 计算两侧值后进一步计算
                Object left = ((ASTreeExtend) left()).eval(env);
                Object right = ((ASTreeExtend) right()).eval(env);
                return computeOp(left, op, right);
            }
        }

        protected Object computeAssign(Environment env, Object rvalue) throws SyntaxException {
            ASTree l = left();
            if (l instanceof Name) {
                if(env.get(((Name)l).getName()) == null){
                    throw new PebbleException("undefined name: " + ((Name)l).getName(), this);
                }
                env.put(((Name) l).getName(), rvalue);
                return rvalue;
            } else
                throw new PebbleException("bad assignment", this);
        }

        protected Object computeOp(Object left, String op, Object right) {
            if (left instanceof Integer && right instanceof Integer) {
                // 两侧都是数字才进行计算
                return computeNumber((Integer) left, op, (Integer) right);
            } else if (op.equals("+"))
                // 若有不是数字的, 且需要相加, 说明是字符串合并
                return String.valueOf(left) + String.valueOf(right);
            else if (op.equals("==")) {
                if (left == null)
                    return right == null ? TRUE : FALSE;
                else
                    return left.equals(right) ? TRUE : FALSE;
            } else
                throw new PebbleException("bad type", this);
        }

        protected Object computeNumber(Integer left, String op, Integer right) {
            int a = left.intValue();
            int b = right.intValue();
            if (op.equals("+"))
                return a + b;
            else if (op.equals("-"))
                return a - b;
            else if (op.equals("*"))
                return a * b;
            else if (op.equals("/"))
                return a / b;
            else if (op.equals("%"))
                return a % b;
            else if (op.equals("=="))
                return a == b ? TRUE : FALSE;
            else if (op.equals(">"))
                return a > b ? TRUE : FALSE;
            else if (op.equals("<"))
                return a < b ? TRUE : FALSE;
            else
                throw new PebbleException("bad operator", this);
        }
    }

    @Reviser
    public static class BlockExtend extends BlockStatement {
        public BlockExtend(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            Object result = 0;
            // this放在这, 将会调用父类的iterator方法
            for (ASTree t : this) {
                if (!(t instanceof NullStatement))
                    result = ((ASTreeExtend) t).eval(env);
            }
            return result;
        }
    }

    @Reviser
    public static class VarExtend extends VarExpression{
        public VarExtend(List<ASTree> children) {
            super(children);
        }

        public Object eval(Environment env) throws SyntaxException {
//            Environment Env =
            for(ASTree t : this){
                if(t.numChildren() == 2){
                    if(t.child(0) instanceof Name && ((Name) t.child(0)).getName() != ","){
                        ((EnvironmentExtend)env).putNew(((Name) t.child(0)).getName(), ((ASTreeExtend)t.child(1)).eval(env));
                    }else{
                        throw new SyntaxException("bad definition",t);
                    }
                }else{
                    if(((Name) t).getName() != ","){
                        env.put(((Name) t).getName(), NaN);
                    }
                }
            }
            return 0;
        }
    }


    @Reviser
    public static class IfExtend extends IfStatement {
        public IfExtend(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) throws SyntaxException {



            Object c = ((ASTreeExtend) condition()).eval(env);
            if (c instanceof Integer) {
                if (((Integer) c).intValue() == TRUE) {
                    return ((ASTreeExtend) thenBlock()).eval(makeEnv(env));
                } else {
                    List<ASTree> list = elseBlock();
                    if(list == null)return 0;
                    // 处理elif 和 else
                    for(ASTree item : list){
                        if(item.numChildren() == 2){
                            // 有两个child, 说明是elif 的情况
                            Object c1 = ((ASTreeExtend) item.child(0)).eval(makeEnv(env));
                            if(c1 instanceof Integer){
                                if(((Integer) c1).intValue() == TRUE){
                                    return ((ASTreeExtend)item.child(1)).eval(makeEnv(env));
                                }
                            }
                        }else{
                            // 只有一个child, 是else的情况
                            return ((ASTreeExtend)item.child(0)).eval(env);
                        }
                    }
                    return 0;
                }
            } else {
                throw new SyntaxException("string condition not allowed", condition().location());
            }
        }
    }

    @Reviser
    public static class WhileExtend extends WhileStatement {
        public WhileExtend(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) throws SyntaxException {
            Object result = 0;
            while (true) {
                Object c = ((ASTreeExtend) condition()).eval(env);
                if (c instanceof Integer){
                    if(((Integer) c).intValue() == FALSE)
                        return result;
                    else{
                        result = ((ASTreeExtend) body()).eval(makeEnv(env));
                    }
                }else{
                    throw new SyntaxException("string condition not allowed", condition().location());
                }
            }
        }
    }
}
