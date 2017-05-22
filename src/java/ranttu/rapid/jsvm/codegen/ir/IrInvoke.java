/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrInvoke.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrInvoke extends IrNode {
    public InvokeType type;

    // for java invoke only
    public String     invokeeName;
    public String     desc;
    public String     className;

    public static IrInvoke invokeStatic(String className, String name, String desc) {
        IrInvoke irInvoke = new IrInvoke();
        irInvoke.type = InvokeType.STATIC;
        irInvoke.className = className;
        irInvoke.invokeeName = name;
        irInvoke.desc = desc;

        return irInvoke;
    }

    public static IrInvoke invokeInit(String className, String desc) {
        IrInvoke irInvoke = new IrInvoke();
        irInvoke.type = InvokeType.SPECIAL;
        irInvoke.invokeeName = MethodConst.INIT;
        irInvoke.desc = desc;
        irInvoke.className = className;

        return irInvoke;
    }

    public static IrInvoke unboundedInvoke() {
        IrInvoke irInvoke = new IrInvoke();
        irInvoke.type = InvokeType.UNBOUNDED_FUNC_CALL;

        return irInvoke;
    }

    public static IrInvoke boundedInvoke() {
        IrInvoke irInvoke = new IrInvoke();
        irInvoke.type = InvokeType.BOUNDED_FUNC_CALL;

        return irInvoke;
    }

    public static IrInvoke makeFunc() {
        return invokeVirtual($$.getInternalName(JsFunctionObject.class), "makeFunction",
            $$.getMethodDescriptor(void.class));
    }

    public static IrInvoke construct() {
        IrInvoke irInvoke = new IrInvoke();
        irInvoke.type = InvokeType.CONSTRUCT;

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
