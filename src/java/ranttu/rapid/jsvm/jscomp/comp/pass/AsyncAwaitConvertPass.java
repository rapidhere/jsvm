package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.tree.LabelNode;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.ir.*;
import ranttu.rapid.jsvm.common.$$;

import java.util.ArrayList;
import java.util.List;

/**
 * convert the async/await pass to normal formal
 * i.e. collect IrReturn.await() nodes and generate the jump table in entry
 *
 * @author rapidhere@gmail.com
 * @version $id: CollectNamingPass.java, v0.1 2017/4/19 dongwei.dq Exp $
 */
public class AsyncAwaitConvertPass extends IrBasedCompilePass {
    private List<IrReturn> asyncLabels;

    protected void visit(ClassNode clazz) {
        asyncLabels = new ArrayList<>();
        clazz.methods().forEach(this::visit);

        // generate entry point
        if (clazz.isAsyncFunction) {
            IrLabel endLabel = IrLabel.label(), beginLabel = IrLabel.label();
            LabelNode[] labels = new LabelNode[asyncLabels.size() + 1];

            // begin label
            labels[0] = beginLabel.label;
            for(int i = 1;i < labels.length;i ++) {
                IrReturn ret = asyncLabels.get(i - 1);
                // assign async point
                ret.asyncPoint = i - 1;
                labels[i] = ret.label.label;
            }

            clazz.method("entry").irPrepend(
                // convert closure0 to closure
                IrLoad.local("closure0"),
                IrCast.cast($$.getInternalName(clazz.getClosureClass())),
                IrStore.local("closure"),

                // prepend label: generate jump table
                IrLoad.local("entryPoint", int.class),
                IrSwitch.switchTable(0, asyncLabels.size(), endLabel.label, labels),

                // first entry point
                beginLabel
            ).ir(
                // end label: throw new RuntimeException("wrong entry point number");
                endLabel,
                IrNew.newObject($$.getInternalName(RuntimeException.class)),
                IrDup.dup(),
                IrLiteral.of("wrong entry point number!"),
                IrInvoke.invokeInit(
                    $$.getInternalName(RuntimeException.class),
                    $$.getMethodDescriptor(void.class, String.class)),
                IrThrow.athrow()
            );
        }

        clazz.innerClasses().forEach(this::visit);
    }

    @Override
    protected void visit(IrReturn irReturn) {
        if (method.parent.isAsyncFunction) {
           if (irReturn.isAwait) {
               // assign entry point label
               irReturn.label = IrLabel.label();
               asyncLabels.add(irReturn);
           }
        }
    }
}
