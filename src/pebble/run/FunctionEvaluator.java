package pebble.run;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import pebble.ast.node.*;
import pebble.exception.PebbleException;
import pebble.exception.SyntaxException;

import static pebble.run.BasicEvaluator.*;

import java.util.List;

// Require 代表执行该类前, 需要先去执行哪些修改器
@Require(BasicEvaluator.class)
@Reviser
public class FunctionEvaluator {
    @Reviser
    public static interface EnvironmentExtend extends Environment {
        void putNew(String name, Object value);

        Environment where(String name);

        void setOuter(Environment e);
    }

    @Reviser
    public static class DefStatementExtend extends DefStatement {
        public DefStatementExtend(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            // 当遇到def时, 要将该函数put到环境中
            ((EnvironmentExtend) env).putNew(name(), new Function(parameters(), body(), env));
            return name();
        }
    }

    @Reviser
    public static class PrimaryExtend extends PrimaryExpression {
        public PrimaryExtend(List<ASTree> c) {
            super(c);
        }

        public ASTree operand() {
            return child(0);
        }

        public Postfix postfix(int nest) {
            return (Postfix) child(numChildren() - nest - 1);
        }

        // numChildren()大于1代表有实参后缀, 要依次调用函数
        public boolean hasPostfix(int nest) {
            return numChildren() - nest > 1;
        }

        public Object eval(Environment env) {
            return evalSubExpression(env, 0);
        }

        public Object evalSubExpression(Environment env, int nest) {
            if (hasPostfix(nest)) {
                // 如果有实参后缀, 先去获取函数信息
                Object functionInfo = evalSubExpression(env, nest + 1);
                // 获取完信息后, 交由Arguments去执行
                // 因为Arguments实现了Postfix的抽象方法
                return ((PostfixExtend) postfix(nest)).eval(env, functionInfo);
            } else
                // operand()将返回函数名或primary, eval将根据名字去环境里去取值
                // 函数名取值的结果就是函数相关信息
                return ((ASTreeExtend) operand()).eval(env);
        }
    }

    @Reviser
    public static abstract class PostfixExtend extends Postfix {
        public PostfixExtend(List<ASTree> c) {
            super(c);
        }

        public abstract Object eval(Environment env, Object value);
    }

    @Reviser
    public static class ArgumentsExtend extends Arguments {
        public ArgumentsExtend(List<ASTree> c) {
            super(c);
        }

        // value 是函数有关信息
        public Object eval(Environment callerEnv, Object functionInfo) {
            if (!(functionInfo instanceof Function))
                throw new PebbleException("bad function", this);
            Function func = (Function) functionInfo;
            ParameterList params = func.parameters();
            if (size() != params.size())
                // 参数数量不对, 报错
                throw new PebbleException("bad number of arguments", this);

            Environment newEnv = func.makeEnv();
            int num = 0;
            for (ASTree a : this)
                // 先将局部变量put进环境里
                ((ParamsExtend) params).eval(newEnv, num++,
                        ((ASTreeExtend) a).eval(callerEnv));
            return ((BlockExtend) func.body()).eval(newEnv);
        }
    }

    @Reviser
    public static class ParamsExtend extends ParameterList {
        public ParamsExtend(List<ASTree> c) {
            super(c);
        }

        public void eval(Environment env, int index, Object value) {
            ((EnvironmentExtend) env).putNew(name(index), value);
        }
    }
}
