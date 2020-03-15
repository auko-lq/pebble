package pebble.ast.integration;

import pebble.exception.PebbleException;
import pebble.run.environment.Environment;

import javax.swing.*;
import java.lang.reflect.Method;

public class Natives {
    public Environment environment(Environment env) {
        appendNatives(env);
        return env;
    }

    // 向环境中添加本地方法
    protected void appendNatives(Environment env) {
        append(env, "println", Natives.class, "println", Object.class);
        append(env, "print", Natives.class, "print", Object.class);
        append(env, "read", Natives.class, "read");
        append(env, "length", Natives.class, "length", Object.class);
        append(env, "toInt", Natives.class, "toInt", Object.class);
        append(env, "currentTime", Natives.class, "currentTime");
    }

    protected void append(Environment env, String name, Class<?> clazz,
                          String methodName, Class<?>... params) {
        Method m;
        try {
            m = clazz.getMethod(methodName, params);
        } catch (Exception e) {
            throw new PebbleException("cannot find a native function: "
                    + methodName);
        }
        env.put(name, new NativeFunction(methodName, m));
    }

    // native methods

    private static boolean newLine = false;
    public static int println(Object obj) {
        if(newLine) System.out.println();
        System.out.println(obj.toString());
        newLine = false;
        return 0;
    }

    public static int print(Object obj) {
        System.out.print(obj.toString());
        newLine = true;
        return 0;
    }

    public static String read() {
        return JOptionPane.showInputDialog(null);
    }

    public static int length(Object s) {
        if(s instanceof Object[]){
            return ((Object[])s).length;
        }
        if(s instanceof String){
            return ((String) s).length();
        }
        throw new PebbleException();
    }

    public static int toInt(Object value) {
        if (value instanceof String)
            return Integer.parseInt((String) value);
        else if (value instanceof Integer)
            return ((Integer) value).intValue();
        else
            throw new NumberFormatException(value.toString());
    }

    private static long startTime = System.currentTimeMillis();

    public static int currentTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }
}
