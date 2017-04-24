/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

/**
 * the ir dup instruction
 *
 * @author rapidhere@gmail.com
 * @version $id: IrDup.java, v0.1 2017/4/24 dongwei.dq Exp $
 */
public class IrDup extends IrNode {
    public IrNode duplicate;

    public static IrDup dup(IrNode dup) {
        IrDup d = new IrDup();
        d.duplicate = dup;

        return d;
    }
}
