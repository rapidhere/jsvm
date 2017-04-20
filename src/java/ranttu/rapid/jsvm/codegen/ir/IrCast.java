/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

/**
 * a cast node
 *
 * @author rapidhere@gmail.com
 * @version $id: IrCast.java, v0.1 2017/4/20 dongwei.dq Exp $
 */
public class IrCast extends IrNode {
    public IrNode from;
    public String name;

    public static IrCast of(IrNode from, String name) {
        IrCast cast = new IrCast();
        cast.from = from;
        cast.name = name;

        return cast;
    }
}
