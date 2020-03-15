package pebble.ast.node;

import pebble.run.environment.Environment;
import pebble.run.environment.NestedEnvironment;

import java.util.List;

public class WhileStatement extends ASTList {
    public WhileStatement(List<ASTree> c) { super(c); }
    public ASTree condition() { return child(0); }
    public ASTree body() { return child(1); }
    public String toString() {
        return "(while " + condition() + " " + body() + ")";
    }
    public Environment makeEnv(Environment env){
        return new NestedEnvironment(env);
    }
}
