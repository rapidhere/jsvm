package ranttu.rapid.jsvm.codegen.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * block of ir
 *
 * @author rapidhere@gmail.com
 * @version $id: IrStore.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrBlock extends IrNode {
    public List<IrNode> irs = new ArrayList<>();

    public IrBlock(IrNode...irs) {
        Collections.addAll(this.irs, irs);
    }

    public static IrBlock of(IrNode...irs) {
        return new IrBlock(irs);
    }
}
