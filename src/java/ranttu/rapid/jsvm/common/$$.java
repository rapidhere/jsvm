/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.common;

/**
 * utils
 *
 * @author rapidhere@gmail.com
 * @version $id: $$.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
final public class $$ {
    // forbidden constructor
    private $$() {
    }

    /**
     * cast object into a required type
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }

    public static <T> T notSupport() {
        throw new RuntimeException("not supported in this method");
    }

    public static <T> T shouldNotReach() {
        return shouldNotReach(null);
    }

    public static <T> T shouldNotReach(Throwable e) {
        throw new RuntimeException("should not reach here", e);
    }

    // ~~~ assert utils
    public static <T> T notNull(T o) {
        if (o == null) {
            throw new AssertionError("object cannot be null");
        }

        return o;
    }

    public static void should(boolean b) {
        if(! b) {
            throw new AssertionError();
        }
    }

    public static String notBlank(String s) {
        if (s == null || s.trim().length() == 0) {
            throw new AssertionError("string cannot be blank");
        }

        return s;
    }

    // string utils
    public static boolean isBlank(String s) {
        return s.trim().length() == 0;
    }
}
