package pebble.run.environment;


public interface Environment {
    Object get(String name);
    void put(String name, Object value);
}
