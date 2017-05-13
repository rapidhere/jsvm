package ranttu.rapid.jsvm.codegen.ir;

/**
 * throw an exception
 *
 * @author rapidhere@gmail.com
 * @version $id: IrThrow.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrThrow extends IrNode {
    public static IrThrow athrow() {
        return new IrThrow();
    }
}
