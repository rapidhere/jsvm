/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrNew.java, v0.1 2017/5/8 dongwei.dq Exp $
 */
public class IrNew extends IrNode {
    public String className;

    public static IrNew newObject(String className) {
        IrNew ir = new IrNew();
        ir.className = className;
        return ir;
    }
}
