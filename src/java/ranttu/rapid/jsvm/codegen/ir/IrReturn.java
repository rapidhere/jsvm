/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import java.util.Optional;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrReturn.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrReturn extends IrNode {
    public Optional<IrNode> exp;

    public IrReturn(IrNode exp) {
        this.exp = Optional.ofNullable(exp);
    }

    public static IrReturn ret() {
        return new IrReturn(null);
    }

    public static IrReturn ret(IrNode exp) {
        return new IrReturn(exp);
    }
}
