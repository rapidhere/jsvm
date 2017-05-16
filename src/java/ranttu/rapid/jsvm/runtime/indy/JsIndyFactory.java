/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime.indy;

import ranttu.rapid.jsvm.common.SystemProperty;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author rapidhere@gmail.com
 * @version $id: JsIndyFactory.java, v0.1 2017/4/15 dongwei.dq Exp $
 */
final public class JsIndyFactory {
    /**
     * call site factory entry for `invokedynamic` instruction
     */
    @SuppressWarnings("unused")
    public static CallSite callsite(MethodHandles.Lookup lookup, String methodName, MethodType mt) {
        JsIndyBaseCallSite callSite;
        if (SystemProperty.Jsvm_UseOptimisticCallSite) {
            callSite = new JsIndyOptimisticCallSite(JsIndyType.valueOf(methodName), mt);
        } else {
            callSite = new JsIndyCallSite(JsIndyType.valueOf(methodName), mt);
        }

        callSite.init();
        return callSite;
    }
}
