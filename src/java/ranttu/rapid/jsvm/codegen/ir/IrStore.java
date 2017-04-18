/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrStore.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrStore extends IrNode {
    public FieldType type;
    public IrNode context;
    public IrNode key;
    public IrNode value;

    public IrStore(FieldType type, IrNode context, IrNode key, IrNode value) {
        this.type = type;
        this.context = context;
        this.key = key;
        this.value = value;
    }

    public static IrStore field(IrNode context, IrNode key, IrNode value) {
        return new IrStore(FieldType.FIELD, context, key, value);
    }

    public static IrStore field(IrNode context, String key, IrNode value) {
        return new IrStore(FieldType.FIELD, context, IrLiteral.of(key), value);
    }

    public static IrStore staticField(String className, String key, IrNode value) {
        return new IrStore(FieldType.STATIC_FIELD, IrLiteral.of(className), IrLiteral.of(key), value);
    }
}
