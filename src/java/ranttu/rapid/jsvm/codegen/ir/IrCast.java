/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import ranttu.rapid.jsvm.common.$$;

/**
 * a cast node
 *
 * @author rapidhere@gmail.com
 * @version $id: IrCast.java, v0.1 2017/4/20 dongwei.dq Exp $
 */
public class IrCast extends IrNode {
    public String name;

    public static IrCast cast(String name) {
        IrCast cast = new IrCast();
        cast.name = name;

        return cast;
    }

    public static IrCast cast(Class clazz) {
        return cast($$.getInternalName(clazz));
    }
}
