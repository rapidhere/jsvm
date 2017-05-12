/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.common;

import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.codegen.ClassNode;

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

    /**
     * cast object into a required type
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o, @SuppressWarnings("unused") Class<T> clazz) {
        return (T) o;
    }

    // ~~~ collections
    @SafeVarargs
    public static <T> boolean in(T o, T... others) {
        for (T other : others) {
            if (other.equals(o)) {
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
        if (!b) {
            throw new AssertionError();
        }
    }

    @SafeVarargs
    public static <T> void shouldIn(T o, T... others) {
        should(in(o, others));
    }

    // ~~~ filter utils
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

    //~~~ byte code helpers

    /**
     * get a internal name from a Class Object/ ClassNode / String
     */
    public static String getInternalName(Object o) {
        if (o instanceof Class) {
            return Type.getInternalName(cast(o, Class.class));
        } else if (o instanceof ClassNode) {
            return cast(o, ClassNode.class).$.name;
        } else {
            return cast(o, String.class).replace('.', '/');
        }
    }

    /**
     * get a descriptor from a Class Object / ClassNode / String
     */
    public static String getDescriptor(Object o) {
        if (o instanceof Class) {
            return Type.getDescriptor(cast(o, Class.class));
        } else if (o instanceof ClassNode) {
            return "L" + cast(o, ClassNode.class).$.name + ";";
        } else {
            String ret = cast(o);
            if(ret.startsWith("L") && ret.endsWith(";")) {
                return ret;
            } else {
                ret = getInternalName(ret);
                return "L" + ret + ";";
            }
        }
    }

    /**
     * get the type of a Class Object / String
     */
    public static Type getType(Object o) {
        return Type.getType(getDescriptor(o));
    }

    /**
     * get the method descriptor, any parameters can be Class Object/ ClassNode / String
     */
    public static String getMethodDescriptor(Object retType, Object...parameterTypes) {
        Type[] types = new Type[parameterTypes.length];
        for(int i = 0;i < types.length;i ++) {
            types[i] = getType(parameterTypes[i]);
        }

        return Type.getMethodDescriptor(getType(retType), types);
    }
}
