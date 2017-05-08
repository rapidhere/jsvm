/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrInvoke.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrInvoke extends IrNode {
    public InvokeType type;

    // for bounded/unbounded/construct invoke only
    public int        numberOfArgs;

    // for java invoke only
    public String     invokeeName;
    public String     desc;
    public String     className;

    public static IrInvoke invokeInit(String className, String desc) {
        IrInvoke irInvoke = new IrInvoke();
        irInvoke.type = InvokeType.SPECIAL;
        irInvoke.invokeeName = MethodConst.INIT;
        irInvoke.desc = desc;
        irInvoke.className = className;

        return irInvoke;
    }

    public static IrInvoke unboundedInvoke(int numberOfArgs) {
        IrInvoke irInvoke = new IrInvoke();
        irInvoke.type = InvokeType.UNBOUNDED_FUNC_CALL;
        irInvoke.numberOfArgs = numberOfArgs;

        return irInvoke;
    }

    public static IrInvoke boundedInvoke(int numberOfArgs) {
        IrInvoke irInvoke = new IrInvoke();
        irInvoke.type = InvokeType.BOUNDED_FUNC_CALL;
        irInvoke.numberOfArgs = numberOfArgs;

        return irInvoke;
    }

    public static IrInvoke makeFunc() {
        return invokeVirtual(Type.getInternalName(JsFunctionObject.class), "makeFunction",
            Type.getMethodDescriptor(Type.VOID_TYPE));
    }

    public static IrInvoke construct(int numberOfArgs) {
        IrInvoke irInvoke = new IrInvoke();
        irInvoke.type = InvokeType.CONSTRUCT;
        irInvoke.numberOfArgs = numberOfArgs;

        return irInvoke;
    }

    public static IrInvoke invokeVirtual(String className, String name, String desc) {
        IrInvoke invoke = new IrInvoke();
        invoke.type = InvokeType.VIRTUAL;
        invoke.className = className;
        invoke.invokeeName = name;
        invoke.desc = desc;

        return invoke;
    }
}
