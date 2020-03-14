package pebble.ast.node;

import java.util.List;

public class PrimaryExpression extends ASTList {
    public PrimaryExpression(List<ASTree> c) { super(c); }

    // create方法在于当Parser库创建节点对象时, 如果该对象有create方法, 则调用它
    // 如果该节点子节点只有一个的话, 就直接返回子节点, 这样的好处是, 避免了生成多余节点
    public static ASTree create(List<ASTree> c) {
        return c.size() == 1 ? c.get(0) : new PrimaryExpression(c);
    }
}
