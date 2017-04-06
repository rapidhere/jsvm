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
 * @version $id: $.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
final public class $ {
    // forbidden constructor
    private $(){
    }

    /**
     * cast object into a required type
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }
}
