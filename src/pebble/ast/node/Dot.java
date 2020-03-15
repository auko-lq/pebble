package pebble.ast.node;

import java.util.List;

public class Dot extends Postfix {
    public Dot(List<ASTree> c) { super(c); }
    public String name() { return ((ASTLeaf)child(0)).getToken().getText(); }
    public String toString() { return "." + name(); } 
}
