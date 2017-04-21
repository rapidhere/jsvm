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
    abstract public JsObjectObject construct(Object... args);


    protected JsFunctionObject() {
        JsObjectObject proto = new JsObjectObject();
        proto.setProperty("constructor", this);
        setProperty("prototype", proto);
    }
}
