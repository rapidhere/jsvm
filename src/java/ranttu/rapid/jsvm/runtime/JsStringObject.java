/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

/**
 * a js string object
 *
 * @author rapidhere@gmail.com
 * @version $id: JsStringObject.java, v0.1 2017/4/14 dongwei.dq Exp $
 */
public class JsStringObject {
    private String value;

    public JsStringObject(String value) {
        this.value = value;
    }

    public String asString() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof JsStringObject
               && asString().equals(((JsStringObject) object).asString());
    }

    @Override
    public String toString() {
        return value;
    }
}
