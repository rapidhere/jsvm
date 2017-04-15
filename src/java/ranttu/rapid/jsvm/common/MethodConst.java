/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.common;

import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.runtime.JsIndyFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author rapidhere@gmail.com
 * @version $id: MethodConst.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
public interface MethodConst {
    String INIT               = "<init>";

    String CLINIT             = "<clinit>";

    String SET_PROPERTY       = "setProperty";

    String GET_PROPERTY       = "getProperty";

    // indy support
    Class  INDY_FACTORY_CLASS = JsIndyFactory.class;

    Handle INDY_JSOBJ_FACTORY = new Handle(
                                  // the invoke type, STATIC or SPECIAL or something else
                                  Opcodes.H_INVOKESTATIC,
                                  // the internal name of factory class
                                  Type.getInternalName(INDY_FACTORY_CLASS),
                                  // the factory method name
                                  "callsite",
                                  // the factory method desc
                                  Type.getMethodDescriptor(ReflectionUtil.getMethod(
                                      JsIndyFactory.class, "callsite", MethodHandles.Lookup.class,
                                      String.class, MethodType.class, MethodType.class)));
}
