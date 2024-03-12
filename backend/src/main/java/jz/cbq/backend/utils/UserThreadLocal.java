package jz.cbq.backend.utils;

/**
 * 用户线程
 *
 * @author caobaoqi
 */
public class UserThreadLocal {
    private UserThreadLocal() {
    }

    private static final ThreadLocal<Object> LOCAL = new ThreadLocal<>();

    public static void put(Object user) {
        LOCAL.set(user);
    }

    public static Object get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}

