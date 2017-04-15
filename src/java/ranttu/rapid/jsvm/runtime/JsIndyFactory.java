/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author rapidhere@gmail.com
 * @version $id: JsIndyFactory.java, v0.1 2017/4/15 dongwei.dq Exp $
 */
final public class JsIndyFactory {
    /**
     * call site factory for JsObjectObject
     */
    @SuppressWarnings("unused")
    public static CallSite callsite(MethodHandles.Lookup lookup, String methodName, MethodType mt,
                                    MethodType actualType) throws NoSuchMethodException,
                                                          IllegalAccessException {
        MethodHandle mh = lookup.findVirtual(JsObjectObject.class, methodName, actualType);
        return new ConstantCallSite(mh.asType(mt));
    }
}
