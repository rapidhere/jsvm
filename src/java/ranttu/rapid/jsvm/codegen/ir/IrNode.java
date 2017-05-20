/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

/**
 * a base ir tree node
 *
 * @author rapidhere@gmail.com
 * @version $id: IrNode.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
abstract public class IrNode {
    public boolean reachFlag = false;

    final public boolean is(Class<? extends IrNode> target) {
        return target.isAssignableFrom(getClass());
    }
}
