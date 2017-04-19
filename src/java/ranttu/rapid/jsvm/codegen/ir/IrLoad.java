/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

/**
 * load instruction
 *
 * @author rapidhere@gmail.com
 * @version $id: IrLoadLocal.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrLoad extends IrNode {
    public FieldType type;
    public IrNode context;
    public IrNode key;

    public IrLoad(FieldType type, IrNode context, IrNode key) {
        this.type = type;
        this.context = context;
        this.key = key;
    }

    public static IrLoad field(IrNode context, IrNode key) {
        return new IrLoad(FieldType.FIELD, context, key);
    }

    public static IrLoad field(IrNode context, String key) {
        return field(context, IrLiteral.of(key));
    }

    public static IrLoad array(IrNode context, int idx) {
        return new IrLoad(FieldType.ARRAY, context, IrLiteral.of(idx));
    }

    public static IrLoad local(String name) {
        return new IrLoad(FieldType.LOCAL, null, IrLiteral.of(name));
    }
}
