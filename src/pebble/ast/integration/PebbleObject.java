package pebble.ast.integration;

import pebble.exception.AccessException;
import pebble.run.environment.Environment;
import pebble.run.evaluator.FunctionEvaluator.EnvironmentExtend;

public class PebbleObject {
    protected Environment env;
    public PebbleObject(Environment e) { env = e; }
    @Override public String toString() { return "<object:" + hashCode() + ">"; }
    public Object read(String member) throws AccessException {
        return getEnv(member).get(member);
    }
    public void write(String member, Object value) throws AccessException {
        ((EnvironmentExtend)getEnv(member)).putNew(member, value);
    }
    protected Environment getEnv(String member) throws AccessException {
        Environment e = ((EnvironmentExtend)env).where(member);
        // 只有环境是 pebbleObject 的环境才正确
        if (e != null && e == env)
            return e;
        else
            throw new AccessException();
    }
}
