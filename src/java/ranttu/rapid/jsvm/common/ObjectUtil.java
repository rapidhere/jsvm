/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.common;

/**
 * object util
 *
 * @author rapidhere@gmail.com
 * @version $id: ObjectUtil.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
final public class ObjectUtil {
    // forbidden constructor
    private ObjectUtil(){
    }

    /**
     * cast object into a required type
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }
}
