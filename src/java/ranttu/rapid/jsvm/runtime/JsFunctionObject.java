/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

/**
 * a javascript function object
 *
 * @author rapidhere@gmail.com
 * @version $id: JsFunctionObject.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
abstract public class JsFunctionObject extends JsObjectObject {
    protected Object $that;

    /**
     * invoke the function object
     */
    abstract public Object invoke(Object... args);

    public JsFunctionObject(Object $that) {
        this.$that = $that;
    }
}
