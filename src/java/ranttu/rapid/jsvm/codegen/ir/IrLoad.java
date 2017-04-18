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
    public String    name;

    public IrLoad(FieldType type, String name) {
        this.type = type;
        this.name = name;
    }

    public static IrLoad loadThis() {
        return new IrLoad(FieldType.LOCAL, "this");
    }
}
