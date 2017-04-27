/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.runtime.JsObjectObject;

import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrInvoke.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrInvoke extends IrNode {
    public InvokeType type;
    public IrNode     invoker;
    public IrNode     invokeName;

    public String     className;
    public String     desc;
    public IrNode[]   args;

    public IrInvoke(InvokeType type, IrNode invoker, IrNode invokeName, String desc, IrNode... args) {
        this.type = type;
        this.invoker = invoker;
        this.invokeName = invokeName;
        this.desc = desc;
        this.args = args;
    }

    public static IrInvoke invokeInit(IrNode invoker, String desc, IrNode... args) {
        return new IrInvoke(InvokeType.SPECIAL, invoker, IrLiteral.of(MethodConst.INIT), desc, args);
    }

    public static IrInvoke invokeInit(Class clazz, String desc, IrNode... args) {
        return invokeInit(IrLiteral.of(Type.getInternalName(clazz)), desc, args);
    }

    public static IrInvoke invokeInit(Class clazz) {
        return invokeInit(clazz, Type.getMethodDescriptor(Type.VOID_TYPE));
    }

    public static IrInvoke unboundedInvoke(IrNode invoker, IrNode... args) {
        return new IrInvoke(InvokeType.UNBOUNDED_FUNC_CALL, invoker, null, null, args);
    }

    public static IrInvoke unboundedInvoke(IrNode invoker, List<IrNode> args) {
        IrNode[] t = new IrNode[args.size()];
        for (int i = 0; i < args.size(); i++) {
            t[i] = args.get(i);
        }

        return unboundedInvoke(invoker, t);
    }

    public static IrInvoke boundedInvoke(IrNode invoker, IrNode invokeName, IrNode... args) {
        return new IrInvoke(InvokeType.BOUNDED_FUNC_CALL, invoker, invokeName, null, args);
    }

    public static IrInvoke boundedInvoke(IrNode invoker, IrNode invokeName, List<IrNode> args) {
        IrNode[] t = new IrNode[args.size()];
        for (int i = 0; i < args.size(); i++) {
            t[i] = args.get(i);
        }

        return boundedInvoke(invoker, invokeName, t);
    }

    public static IrInvoke makeFunc(String className, IrNode funcObj) {
        return invokeVirtual(funcObj, className, "makeFunction",
            Type.getMethodDescriptor(Type.VOID_TYPE));
    }

    public static IrInvoke construct(String className, IrNode funcObj) {
        return invokeVirtual(funcObj, className, "construct",
            Type.getMethodDescriptor(Type.getType(JsObjectObject.class)));
    }

    public static IrInvoke invokeVirtual(IrNode invoker, String className, String name,
                                         String desc, IrNode... args) {
        IrInvoke invoke = new IrInvoke(InvokeType.VIRTUAL, invoker, IrLiteral.of(name), desc, args);
        invoke.className = className;

        return invoke;
    }
}
