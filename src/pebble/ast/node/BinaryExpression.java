package pebble.ast.node;

import java.util.List;

/** 二元表达式 1 + 2
 *
 * @author: auko
 * @data 2020-03-11 21:19
 */
public class BinaryExpression extends ASTList{
    public BinaryExpression(List<ASTree> children){
        super(children);
    }
    public ASTree left(){return child(0);}
    public String getOperator(){
        return ((ASTLeaf)child(1)).getToken().getText();
    }
    public ASTree right(){return child(2);}
}
