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

    // for set_field usage only
    public String    className;
    public String    desc;
    public String    key;

    public static IrStore property() {
        IrStore ir = new IrStore();
        ir.type = FieldType.PROP;

        return ir;
    }

    public static IrStore field(String className, String fieldName, String desc) {
        IrStore ir = new IrStore();
        ir.type = FieldType.FIELD;
        ir.className = className;
        ir.desc = desc;
        ir.key = fieldName;

        return ir;
    }

    public static IrStore staticField(String className, String fieldName, String desc) {
        IrStore ir = new IrStore();
        ir.type = FieldType.STATIC_FIELD;
        ir.className = className;
        ir.desc = desc;
        ir.key = fieldName;

        return ir;
    }

    public static IrStore local(String name) {
        IrStore ir = new IrStore();
        ir.type = FieldType.LOCAL;
        ir.key = name;

        return ir;
    }

    public static IrStore array() {
        IrStore ir = new IrStore();
        ir.type = FieldType.ARRAY;

        return ir;
    }
}
