/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import jdk.internal.org.objectweb.asm.tree.LabelNode;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrLabel.java, v0.1 2017/5/8 dongwei.dq Exp $
 */
public class IrLabel extends IrNode {
    public LabelNode label;

    public static IrLabel label() {
        IrLabel irLabel = new IrLabel();
        irLabel.label = new LabelNode();

        return irLabel;
    }
}
