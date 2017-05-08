/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrReturn.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrReturn extends IrNode {
    public boolean hasReturnValue = false;

    public static IrReturn ret() {
        return new IrReturn();
    }

    public static IrReturn retWithValue() {
        IrReturn irReturn = new IrReturn();
        irReturn.hasReturnValue = true;
        return irReturn;
    }
}
