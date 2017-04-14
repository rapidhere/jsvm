/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

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
        return object instanceof JsNumberObject
               && asDouble() == ((JsNumberObject) object).asDouble();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
