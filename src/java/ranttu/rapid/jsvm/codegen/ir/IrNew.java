package ranttu.rapid.jsvm.codegen.ir;

import jdk.internal.org.objectweb.asm.Type;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrStore.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrNew extends IrNode {
    public IrNode name;
    public Object[] args = {};
    public String desc;

    public IrNew(IrNode name,String desc, Object...args) {
        this.name = name;
        this.args = args;
        this.desc = desc;
    }

    public static IrNew of(String name, String desc, Object...args) {
        return new IrNew(IrLiteral.of(name), desc, args);
    }

    public static IrNew of(Class clazz, String desc, Object...args) {
        return of(Type.getInternalName(clazz), desc, args);
    }

    public static IrNew of(String name) {
        return of(name, Type.getMethodDescriptor(Type.VOID_TYPE));
    }

    public static IrNew of(Class clazz) {
        return of(Type.getInternalName(clazz));
    }
}
