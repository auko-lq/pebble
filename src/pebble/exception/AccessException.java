package pebble.exception;

/**
 * @author: auko
 * @data 2020-03-15 14:36
 */
public class AccessException  extends PebbleException {

    public AccessException(){
        super("access denied");
    }

    public AccessException(String msg) {
        super(msg);
    }
}
