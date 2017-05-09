/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.common;

import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Opcodes;
import ranttu.rapid.jsvm.runtime.indy.JsIndyFactory;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author rapidhere@gmail.com
 * @version $id: MethodConst.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
public interface MethodConst {
    String INIT               = "<init>";

    String CLINIT             = "<clinit>";

    // indy support
    Class  INDY_FACTORY_CLASS = JsIndyFactory.class;

    Handle INDY_JSOBJ_FACTORY = new Handle(
                                  // the invoke type, STATIC or SPECIAL or something else
                                  Opcodes.H_INVOKESTATIC,
                                  // the internal name of factory class
                                  $$.getInternalName(INDY_FACTORY_CLASS),
                                  // the factory method name
                                  "callsite",
                                  // the factory method desc
                                  $$.getMethodDescriptor(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class));
}
