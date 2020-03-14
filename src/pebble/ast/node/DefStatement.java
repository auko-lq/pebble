package pebble.ast.node;

import java.util.List;

public class DefStatement extends ASTList {
    public DefStatement(List<ASTree> c) { super(c); }
    public String name() { return ((ASTLeaf)child(0)).getToken().getText(); }
    public ParameterList parameters() { return (ParameterList)child(1); }
    public BlockStatement body() { return (BlockStatement)child(2); }
    public String toString() {
        return "(def " + name() + " " + parameters() + " " + body() + ")";
    }
}
