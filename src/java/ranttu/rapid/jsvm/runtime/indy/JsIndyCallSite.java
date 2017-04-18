/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime.indy;

import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsObjectObject;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

/**
 * a invoke-dynamic call site that is mutable
 *
 * @author rapidhere@gmail.com
 * @version $id: JsIndyBindingPoint.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class JsIndyCallSite extends MutableCallSite {
    private JsIndyType   indyType;

    private static MethodHandle SET_PROP;
    private static MethodHandle GET_PROP;

    static {
        try {
            SET_PROP = MethodHandles.lookup().findStatic(JsIndyCallSite.class, "setProperty",
                    MethodType.methodType(void.class, Object.class, String.class, Object.class));

            GET_PROP = MethodHandles.lookup().findStatic(JsIndyCallSite.class, "getProperty",
                    MethodType.methodType(Object.class, Object.class, String.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public JsIndyCallSite(JsIndyType indyType, MethodType type) {
        super(type);
        this.indyType = indyType;
    }

    public void init() {
        switch (indyType) {
            case SET_PROP:
                setTarget(SET_PROP);
                break;
            case GET_PROP:
                setTarget(GET_PROP);
                break;
        }
    }

    @SuppressWarnings("unused")
    public static void setProperty(Object obj, String name, Object val) {
        // TODO: refine
        if(obj instanceof JsObjectObject) {
            $$.cast(obj, JsObjectObject.class).setProperty(name, val);
        } else {
            ReflectionUtil.setFieldValue(obj, name, val);
        }
    }

    @SuppressWarnings("unused")
    public static Object getProperty(Object obj, String name) {
        // TODO: refine
        if(obj instanceof JsObjectObject) {
            return $$.cast(obj, JsObjectObject.class).getProperty(name);
        } else {
            return ReflectionUtil.getFieldValue(obj, name);
        }
    }
}
