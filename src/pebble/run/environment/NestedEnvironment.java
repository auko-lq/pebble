package pebble.run.environment;

import pebble.run.evaluator.FunctionEvaluator.*;

import java.util.HashMap;

public class NestedEnvironment implements Environment {
    protected HashMap<String, Object> values;
    protected Environment outer;

    public NestedEnvironment() {
        this(null);
    }

    public NestedEnvironment(Environment e) {
        values = new HashMap<>();
        outer = e;
    }

    public void setOuter(Environment e) {
        outer = e;
    }

    public Object get(String name) {
        Object v = values.get(name);
        if (v == null && outer != null)
            return outer.get(name);
        else
            return v;
    }

    // 无视外层环境
    public void putNew(String name, Object value) {
        values.put(name, value);
    }


    public void put(String name, Object value) {
        Environment e = where(name);
        if (e == null)
            e = this;
        ((EnvironmentExtend) e).putNew(name, value);
    }

    public Environment where(String name) {
        // 从局部开始找
        if (values.get(name) != null)
            return this;
        else if (outer == null)
            return null;
        else
            return ((EnvironmentExtend) outer).where(name);
    }
}
