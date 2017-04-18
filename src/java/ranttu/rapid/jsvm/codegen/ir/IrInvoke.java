/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.common.MethodConst;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrInvoke.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrInvoke extends IrNode {
    public InvokeType type;
    public IrNode invoker;
    public IrNode invokeName;

    public IrInvoke(InvokeType type, IrNode invoker, IrNode invokeName) {
        this.type = type;
        this.invoker = invoker;
        this.invokeName = invokeName;
    }

    public static IrInvoke invokeInit(IrNode invoker) {
        return new IrInvoke(InvokeType.SPECIAL, invoker, IrLiteral.of(MethodConst.INIT));
    }

    public static IrInvoke invokeInit(Class clazz) {
        return invokeInit(IrLiteral.of(Type.getInternalName(clazz)));
    }
}
