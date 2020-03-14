package pebble.run;


public interface Environment {
    Object get(String name);
    void put(String name, Object value);
}
