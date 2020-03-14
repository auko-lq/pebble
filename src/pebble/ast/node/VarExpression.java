package pebble.ast.node;

import java.util.List;

/**
 * @author: auko
 * @data 2020-03-14 17:17
 */
public class VarExpression extends ASTList{
    public VarExpression(List<ASTree> children) {
        super(children);
    }

    public String toString(){
        return "(var";
    }
}
