package pebble.ast.node;

import java.util.List;

public class Fun extends ASTList {
    public Fun(List<ASTree> c) { super(c); }
    public ParameterList parameters() { return (ParameterList)child(0); }
    public BlockStatement body() { return (BlockStatement)child(1); }
    public String toString() {
        return "(fun " + parameters() + " " + body() + ")";
    }
}
