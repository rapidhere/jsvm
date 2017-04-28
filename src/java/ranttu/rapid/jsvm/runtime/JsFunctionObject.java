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
    /**
     * invoke the function object
     */
    abstract public Object invoke(Object $this, Object... args);

    /**
     * construct the object
     */
    public JsObjectObject construct(Object... args) {
        JsObjectObject obj = JsRuntime.Object.construct();
        invoke(obj, args);
        obj.setProperty("__proto__", getProperty("prototype"));
        return obj;
    }

    /**
     * make the object a callable function
     */
    final public void makeFunction() {
        JsObjectObject proto = new JsObjectObject();
        proto.setProperty("constructor", this);
        proto.setProperty("__proto__", JsRuntime.Object.getProperty("prototype"));

        setProperty("prototype", proto);
        setProperty("__proto__", JsRuntime.Function.getProperty("prototype"));
    }

    protected JsFunctionObject() {
    }
}
