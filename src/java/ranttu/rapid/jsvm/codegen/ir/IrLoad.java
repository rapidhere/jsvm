/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import jdk.internal.org.objectweb.asm.Type;

/**
 * load instruction
 *
 * @author rapidhere@gmail.com
 * @version $id: IrLoadLocal.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrLoad extends IrNode {
    public FieldType type;
    public IrNode    context;
    public IrNode    key;

    // for get_field instruction only
    public String    className;
    public String    desc;

    public IrLoad(FieldType type, IrNode context, IrNode key) {
        this.type = type;
        this.context = context;
        this.key = key;
    }

    public static IrLoad property(IrNode context, IrNode key) {
        return new IrLoad(FieldType.PROP, context, key);
    }

    public static IrLoad property(IrNode context, String key) {
        return property(context, IrLiteral.of(key));
    }

    public static IrLoad field(IrNode context, String key, String className, String desc) {
        IrLoad ir = new IrLoad(FieldType.FIELD, context, IrLiteral.of(key));
        ir.className = className;
        ir.desc = desc;
        return ir;
    }

    public static IrLoad staticField(Class clazz, String fieldName, String desc) {
        IrLoad load = new IrLoad(FieldType.STATIC_FIELD, IrLiteral.of(Type.getInternalName(clazz)),
            IrLiteral.of(fieldName));
        load.desc = desc;
        return load;
    }

    public static IrLoad array(IrNode context, int idx) {
        return new IrLoad(FieldType.ARRAY, context, IrLiteral.of(idx));
    }

    public static IrLoad local(String name) {
        return new IrLoad(FieldType.LOCAL, null, IrLiteral.of(name));
    }
}
