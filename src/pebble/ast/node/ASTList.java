package pebble.ast.node;

import java.util.Iterator;
import java.util.List;

/**
 * @author: auko
 * @data 2020-03-11 21:12
 */
public class ASTList extends ASTree{

    private List<ASTree> children;

    public ASTList(List<ASTree> children){
        this.children = children;
    }

    @Override
    public ASTree child(int i) {
        return children.get(i);
    }

    @Override
    public int numChildren() {
        return children.size();
    }

    @Override
    public Iterator<ASTree> children() {
        return children.iterator();
    }

    public List<ASTree> subChildren(int fromIndex, int toIndex){
        return children.subList(fromIndex, toIndex);
    }

    @Override
    public String location() {
        // 递归寻找location
        for (ASTree t: children) {
            String s = t.location();
            if (s != null)
                return s;
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        String sep = "";
        for (ASTree t: children) {
            builder.append(sep);
            sep = " ";
            builder.append(t.toString());
        }
        return builder.append(')').toString();
    }
}
