/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime.indy;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rapidhere@gmail.com
 * @version $id: JsIndyFactory.java, v0.1 2017/4/15 dongwei.dq Exp $
 */
final public class JsIndyFactory {
    /** the indy factory holder for current thread */
    private static final ThreadLocal<JsIndyFactory> INDY_HOLDER   = new ThreadLocal<JsIndyFactory>() {
                                                                      @Override
                                                                      public JsIndyFactory initialValue() {
                                                                          return new JsIndyFactory();
                                                                      }
                                                                  };

    /** binding points mapper */
    private Map<Integer, JsIndyCallSite>            bindingPoints = new HashMap<>();

    /**
     * call site factory entry for `invokedynamic` instruction
     */
    @SuppressWarnings("unused")
    public static CallSite callsite(MethodHandles.Lookup lookup, String methodName, MethodType mt,
                                    MethodType actualType, int bindingPoint) {
        return getCallSite(bindingPoint);
    }

    /**
     * get current thread's factory
     */
    public static JsIndyFactory getFactory() {
        return INDY_HOLDER.get();
    }

    /**
     * get the binding point
     */
    public static JsIndyCallSite getCallSite(int bindingPoint) {
        return getFactory().bindingPoints.get(bindingPoint);
    }

    /**
     * set the binding point
     */
    public static void setCallSite(int bindingPoint, JsIndyCallSite callSite) {
        getFactory().bindingPoints.put(bindingPoint, callSite);
    }
}
