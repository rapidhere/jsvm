/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * the reflection utils
 *
 * @author rapidhere@gmail.com
 * @version $id: ReflectionUtil.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
final public class ReflectionUtil {
    // forbidden constructor
    private ReflectionUtil() {
    }

    public static boolean isSingleAbstractMethod(Class clazz) {
        return clazz.getMethods().length == 1 && clazz.isInterface();
    }

    public static Method getSingleAbstractMethod(Class clazz) {
        if (isSingleAbstractMethod(clazz)) {
            return clazz.getMethods()[0];
        } else {
            for(Class i: clazz.getInterfaces()) {
                if(isSingleAbstractMethod(i)) {
                    return i.getMethods()[0];
                }
            }
        }

        return null;
    }

    public static Field getField(Object instance, String fieldName) {
        Class clazz = instance.getClass();

        while (true) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
                if (clazz == Object.class) {
                    throw new RuntimeException(e);
                } else {
                    clazz = clazz.getSuperclass();
                }
            }
        }
    }

    public static Method getMethodWithName(Class clazz, String name) {
        for(Method method: clazz.getMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }

        throw new RuntimeException("not found");
    }

    public static void setFieldValue(Object instance, String fieldName, Object value) {
        Field field = getField(instance, fieldName);

        boolean rawAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            $$.shouldNotReach(e);
        } finally {
            field.setAccessible(rawAccessible);
        }
    }

    public static <T> T getFieldValue(Object instance, String fieldName) {
        Field field = getField(instance, fieldName);

        boolean rawAccessible = field.isAccessible();
        try {
            field.setAccessible(true);

            return $$.cast(field.get(instance));
        } catch (IllegalAccessException e) {
            return $$.shouldNotReach(e);
        } finally {
            field.setAccessible(rawAccessible);
        }
    }

    public static Field getStaticField(Class clazz, String fieldName) {
        try {
            return clazz.getField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getStaticValue(Class clazz, String fieldName) {
        Field field = getStaticField(clazz, fieldName);

        try {
            return $$.cast(field.get(null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class... pars) {
        try {
            return clazz.getDeclaredConstructor(pars);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(Class clazz, String methodName, Class... pars) {
        try {
            return clazz.getMethod(methodName, pars);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
