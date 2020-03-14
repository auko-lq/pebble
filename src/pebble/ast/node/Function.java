package pebble.ast.node;


import pebble.run.Environment;
import pebble.run.NestedEnvironment;

public class Function {
    protected ParameterList parameters;
    protected BlockStatement body;
    protected Environment env;
    public Function(ParameterList parameters, BlockStatement body, Environment env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;
    }
    public ParameterList parameters() { return parameters; }
    public BlockStatement body() { return body; }
    public Environment makeEnv() { return new NestedEnvironment(env); }
    @Override public String toString() { return "<fun:" + hashCode() + ">"; }
}
