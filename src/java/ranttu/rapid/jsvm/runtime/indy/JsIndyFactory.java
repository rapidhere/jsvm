/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime.indy;

import ranttu.rapid.jsvm.common.SystemProperty;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author rapidhere@gmail.com
 * @version $id: JsIndyFactory.java, v0.1 2017/4/15 dongwei.dq Exp $
 */
final public class JsIndyFactory {
    private JsIndyFactory() {
    }

    /**
     * call site factory entry for `invokedynamic` instruction
     */
    @SuppressWarnings("unused")
    public static CallSite callsite(MethodHandles.Lookup lookup, String methodName, MethodType mt) {
        JsIndyBaseCallSite callSite;
        JsIndyType type = JsIndyType.valueOf(methodName);

        if (SystemProperty.UseOptimisticCallSite) {
            callSite = new JsIndyOptimisticCallSite(type, mt);
        } else {
            callSite = new JsIndyCallSite(type, mt);
        }

        callSite.init();
        return callSite;
    }

    /**
     * call site factory entry for a glue instance between sam method and js function
     */
    @SuppressWarnings("unused")
    public static CallSite samGlueSite(MethodHandles.Lookup lookup, String methodName, MethodType mt) throws Throwable {
        Class samGlueClass = RuntimeCompiling.getSamGlue(mt.returnType());
        return new ConstantCallSite(lookup.findStatic(
            samGlueClass, "getGlueInterface", mt));
    }
}
