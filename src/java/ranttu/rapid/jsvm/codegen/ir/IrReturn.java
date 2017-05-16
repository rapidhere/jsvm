/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrReturn.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrReturn extends IrNode {
    public boolean hasReturnValue = false;
    public boolean isAwait = false;

    // for await label
    public IrLabel label;
    public int asyncPoint;

    public List<String> restStack;

    public static IrReturn ret() {
        return new IrReturn();
    }

    public static IrReturn retWithValue() {
        IrReturn irReturn = new IrReturn();
        irReturn.hasReturnValue = true;
        return irReturn;
    }

    public static IrReturn await() {
        IrReturn irReturn = new IrReturn();
        irReturn.isAwait = true;
        return irReturn;
    }
}
