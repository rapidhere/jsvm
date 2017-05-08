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
    public static IrDup dup() {
        return new IrDup();
    }
}
