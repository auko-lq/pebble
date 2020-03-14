package pebble.ast.node;

import java.util.List;

public class ParameterList extends ASTList {
    public ParameterList(List<ASTree> c) {
        super(c);
    }

    public String name(int i) {
        return ((ASTLeaf) child(i)).getToken().getText();
    }

    public int size() {
        return numChildren();
    }
}
