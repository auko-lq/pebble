package pebble.ast.node;

import java.util.List;

public class NegativeExpression extends ASTList {
    public NegativeExpression(List<ASTree> c) { super(c); }
    public ASTree operand() { return child(0); }
    public String toString() {
        return "-" + operand();
    }
}
