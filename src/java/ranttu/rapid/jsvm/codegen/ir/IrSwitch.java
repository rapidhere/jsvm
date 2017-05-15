package ranttu.rapid.jsvm.codegen.ir;

import jdk.internal.org.objectweb.asm.tree.LabelNode;

/**
 * the switch instruction
 *
 * @author rapidhere@gmail.com
 * @version $id: IrSwitch.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public class IrSwitch extends IrNode {
    public int min, max;
    public LabelNode defaultLabel;
    public LabelNode[] labels;

    public static IrSwitch switchTable(int min, int max, LabelNode dl, LabelNode...labels) {
        IrSwitch ir = new IrSwitch();

        ir.min = min;
        ir.max = max;
        ir.defaultLabel = dl;
        ir.labels = labels;

        return ir;
    }
}
