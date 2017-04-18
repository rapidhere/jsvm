/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import ranttu.rapid.jsvm.common.MethodConst;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrInvoke.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrInvoke extends IrNode {
    public InvokeType type;
    public String name;

    public IrInvoke(InvokeType type, String name) {
        this.type = type;
        this.name = name;
    }

    public static IrInvoke invokeInit() {
        return new IrInvoke(InvokeType.SPECIAL, MethodConst.INIT);
    }
}
