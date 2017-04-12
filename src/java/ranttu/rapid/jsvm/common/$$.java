/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.common;

import java.util.Optional;

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

    // ~~~ collections
    @SafeVarargs
    public static <T> boolean in(T o, T... others) {
        for(T other: others) {
            if(other.equals(o)) {
                return true;
            }
        }

        return false;
    }

    @SafeVarargs
    public static <T> boolean notIn(T o, T... others) {
        return !in(o, others);
    }

    // ~~~ assertion errors
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
    public static void should(boolean b) {
        if(! b) {
            throw new AssertionError();
        }
    }

    @SafeVarargs
    public static  <T> void shouldIn(T o, T ...others) {
        should(in(o, others));
    }

    // ~~~ filter utils
    public static String notBlank(String s) {
        if (s == null || s.trim().length() == 0) {
            throw new AssertionError("string cannot be blank");
        }

        return s;
    }

    public static <T> T notNull(T o) {
        if (o == null) {
            throw new AssertionError("object cannot be null");
        }

        return o;
    }

    public static <T> T present(Optional<T> op) {
        if(op.isPresent()) {
            return op.get();
        }

        throw new AssertionError("object must present");
    }

    // string utils
    public static boolean isBlank(String s) {
        return s.trim().length() == 0;
    }
}
