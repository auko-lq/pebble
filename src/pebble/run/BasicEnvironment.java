package pebble.run;

import java.util.HashMap;

/**
 * @author: auko
 * @data 2020-03-12 20:53
 */
public class BasicEnvironment implements Environment {
    protected HashMap<String, Object> map;

    public BasicEnvironment(){
        map = new HashMap<>();
    }

    @Override
    public Object get(String name) {
        return map.get(name);
    }

    @Override
    public void put(String name, Object value) {
        map.put(name, value);
    }
}
