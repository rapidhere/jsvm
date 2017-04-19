/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

import ranttu.rapid.jsvm.common.$$;

/**
 * the js number object
 *
 * @author rapidhere@gmail.com
 * @version $id: JsNumberObject.java, v0.1 2017/4/14 dongwei.dq Exp $
 */
public class JsNumberObject {
    private double value;

    public JsNumberObject(int number) {
        this.value = number;
    }

    public JsNumberObject(double number) {
        this.value = number;
    }

    public JsNumberObject(Number number) {
        this.value = number.doubleValue();
    }

    public double asDouble() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof JsNumberObject) {
            return asDouble() == $$.cast(object, JsNumberObject.class).asDouble();
        } else if(object instanceof Number) {
            return asDouble() == $$.cast(object, Number.class).doubleValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
