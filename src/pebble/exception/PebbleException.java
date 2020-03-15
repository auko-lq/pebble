package pebble.exception;

import pebble.ast.node.ASTree;

/**
 * @author: auko
 * @data 2020-03-11 12:04
 */
public class PebbleException extends RuntimeException{
    public PebbleException(){
        super();
    }

    public PebbleException(String msg){
        super(msg);
    }
    public PebbleException(String msg, ASTree tree){
        super(msg + " " + tree.location());
    }
}
