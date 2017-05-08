/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import jdk.internal.org.objectweb.asm.tree.LabelNode;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrIfeq.java, v0.1 2017/5/8 dongwei.dq Exp $
 */
public class IrJump extends IrNode {
    public JumpType type;
    public LabelNode label;

    public static IrJump eq(LabelNode label) {
        IrJump j = new IrJump();
        j.type = JumpType.IF_EQ;
        j.label = label;

        return j;
    }

    public static IrJump j(LabelNode label) {
        IrJump j = new IrJump();
        j.type = JumpType.DIRECT;
        j.label = label;

        return j;
    }
}
