package pebble.ast.node;

import java.util.Iterator;

/**
 * @author: auko
 * @data 2020-03-11 0:51
 */
public abstract class ASTree implements Iterable<ASTree>{
    public abstract ASTree child(int i);

    public abstract int numChildren();

    public abstract Iterator<ASTree> children();

    public abstract String location();

    public Iterator<ASTree> iterator() {
        return children();
    }
}
