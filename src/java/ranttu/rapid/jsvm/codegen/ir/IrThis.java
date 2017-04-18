package ranttu.rapid.jsvm.codegen.ir;

/**
 * this instruction
 *
 * @author rapidhere@gmail.com
 * @version $id: IrLoadLocal.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrThis extends IrNode {
    public static IrThis irthis() {
        return new IrThis();
    }
}
