package pebble.run.evaluator;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import pebble.ast.integration.ClassInfo;
import pebble.ast.integration.PebbleObject;
import pebble.ast.node.*;
import pebble.exception.AccessException;
import pebble.exception.SyntaxException;
import pebble.run.environment.Environment;
import pebble.run.environment.NestedEnvironment;
import pebble.run.evaluator.BasicEvaluator.ASTreeExtend;
import pebble.run.evaluator.BasicEvaluator.BinaryExtend;
import pebble.run.evaluator.FunctionEvaluator.PrimaryExtend;

import java.util.List;

@Require(FunctionEvaluator.class)
@Reviser
public class ClassEvaluator {
    @Reviser
    public static class ClassStmntExtend extends ClassStatement {
        public ClassStmntExtend(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            // 执行到class代码块时, 创建classInfo, 主要是为了保存env
            ClassInfo ci = new ClassInfo(this, env);
            env.put(name(), ci);
            return name();
        }
    }

    @Reviser
    public static class ClassBodyExtend extends ClassBody {
        public ClassBodyExtend(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            for (ASTree t : this)
                ((ASTreeExtend) t).eval(env);
            return null;
        }
    }

    @Reviser
    public static class DotExtend extends Dot {
        public DotExtend(List<ASTree> c) {
            super(c);
        }

        // 此处的value是PrimaryExpression类的evalSubExpression调用时传入的"句点左侧值"
        public Object eval(Environment env, Object value) {
            String member = name();
            // 句点左侧是类
            if (value instanceof ClassInfo) {
                // 当'.'后面接着的是new时, 表示创建一个新对象
                if ("new".equals(member)) {
                    ClassInfo ci = (ClassInfo) value;
                    // 包装类本身的环境
                    NestedEnvironment e = new NestedEnvironment(ci.environment());
                    PebbleObject po = new PebbleObject(e);
                    e.putNew("this", po);
                    initObject(ci, e);
                    return po;
                }
                // 句点不是类的话, 就是对象获取字段或调用方法
            } else if (value instanceof PebbleObject) {
                try {
                    return ((PebbleObject) value).read(member);
                } catch (AccessException e) {
                }
            }
            throw new AccessException("bad member access: " + location() + ": " + member);
        }

        protected void initObject(ClassInfo ci, Environment env) {
            if (ci.superClass() != null)
                initObject(ci.superClass(), env);
            // 执行自身或最终父类的代码
            ((ClassBodyExtend) ci.body()).eval(env);
        }
    }

    @Reviser
    public static class AssignExtend extends BinaryExtend {
        public AssignExtend(List<ASTree> c) {
            super(c);
        }

        @Override
        protected Object computeAssign(Environment env, Object rvalue) throws SyntaxException {
            ASTree le = left();
            // 如果左侧是一个primary, 则准备对类的属性进行赋值
            if (le instanceof PrimaryExpression) {
                PrimaryExtend p = (PrimaryExtend) le;
                // 对这个primary进行校验, 看看是不是类似  p.field  这样的形式
                if (p.hasPostfix(0) && p.postfix(0) instanceof Dot) {

                    // 此处是计算最后一个dot前所有值
                    // 如table.get().next.x = 3
                    // t 的值为table.get().next 的最右值
                    Object t = ((PrimaryExtend) le).evalSubExpression(env, 1);
                    if (t instanceof PebbleObject)
                        return setField((PebbleObject) t, (Dot) p.postfix(0),
                                rvalue);
                }
            }
            // 调用原本的方法
            return super.computeAssign(env, rvalue);
        }

        protected Object setField(PebbleObject obj, Dot expr, Object rvalue) {
            String name = expr.name();
            try {
                obj.write(name, rvalue);
                return rvalue;
            } catch (AccessException e) {
                throw new AccessException("bad member access " + location()
                        + ": " + name);
            }
        }
    }
}
