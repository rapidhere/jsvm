/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime.indy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

/**
 * a invoke-dynamic call site that is mutable
 *
 * @author rapidhere@gmail.com
 * @version $id: JsIndyBindingPoint.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class JsIndyCallSite extends MutableCallSite {
    /** the method handle of binding point */
    private MethodHandle methodHandle;

    public JsIndyCallSite(MethodType type) {
        super(type);
    }
}
