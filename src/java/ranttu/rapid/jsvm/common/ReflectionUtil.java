/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

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

    public static Field getField(Object instance, String fieldName) {
        try {
            return instance.getClass().getField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            return clazz.getConstructor(pars);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
