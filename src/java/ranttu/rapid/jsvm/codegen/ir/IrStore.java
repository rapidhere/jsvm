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
    public String    name;

    public IrStore(FieldType type, String name) {
        this.type = type;
        this.name = name;
    }

    public static IrStore field(String name) {
        return new IrStore(FieldType.FIELD, name);
    }
}
