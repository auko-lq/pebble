package pebble.ast.node;

import java.util.List;

public class ArrayReference extends Postfix {
    public ArrayReference(List<ASTree> c) { super(c); }
    public ASTree index() { return child(0); }
    public String toString() { return "[" + index() + "]"; }
}
