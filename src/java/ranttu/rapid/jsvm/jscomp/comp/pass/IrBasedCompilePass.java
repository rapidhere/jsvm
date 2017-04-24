package ranttu.rapid.jsvm.jscomp.comp.pass;

import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.codegen.ir.IrBlock;
import ranttu.rapid.jsvm.codegen.ir.IrCast;
import ranttu.rapid.jsvm.codegen.ir.IrDup;
import ranttu.rapid.jsvm.codegen.ir.IrInvoke;
import ranttu.rapid.jsvm.codegen.ir.IrLiteral;
import ranttu.rapid.jsvm.codegen.ir.IrLoad;
import ranttu.rapid.jsvm.codegen.ir.IrNew;
import ranttu.rapid.jsvm.codegen.ir.IrNode;
import ranttu.rapid.jsvm.codegen.ir.IrReturn;
import ranttu.rapid.jsvm.codegen.ir.IrStore;
import ranttu.rapid.jsvm.codegen.ir.IrThis;
import ranttu.rapid.jsvm.common.$$;

/**
 * the pass that based on ir-tree
 *
 * @author rapidhere@gmail.com
 * @version $id: GenerateBytecodePass.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
abstract public class IrBasedCompilePass extends CompilePass {
    @Override
    public void start() {
        visit(context.rootClassNode);
    }

    // ~~~ visitors
    protected void visit(ClassNode classNode) {
        in(classNode).invoke(() -> {
            classNode.methods().forEach(this::visit);

            classNode.innerClasses().forEach(this::visit);
        });
    }

    protected void visit(MethodNode methodNode) {
        in(methodNode).invoke(() -> visit(methodNode.ir()));
    }

    protected void visit(IrNode irNode) {
        if (irNode.is(IrBlock.class)) {
            visit((IrBlock) irNode);
        } else if (irNode.is(IrReturn.class)) {
            visit((IrReturn) irNode);
        } else if (irNode.is(IrNew.class)) {
            visit((IrNew) irNode);
        } else if (irNode.is(IrStore.class)) {
            visit((IrStore) irNode);
        } else if (irNode.is(IrThis.class)) {
            visit((IrThis) irNode);
        } else if (irNode.is(IrLoad.class)) {
            visit((IrLoad) irNode);
        } else if (irNode.is(IrInvoke.class)) {
            visit((IrInvoke) irNode);
        } else if (irNode.is(IrLiteral.class)) {
            visit((IrLiteral) irNode);
        } else if (irNode.is(IrCast.class)) {
            visit((IrCast) irNode);
        } else if(irNode.is(IrDup.class)) {
            visit((IrDup) irNode);
        } else {
            $$.notSupport();
        }
    }

    protected void visit(IrDup irDup) {
    }

    protected void visit(IrThis irThis) {
    }

    protected void visit(IrBlock block) {
        block.irs.forEach(this::visit);
    }

    protected void visit(IrNew irNew) {
    }

    protected void visit(IrStore irs) {
    }

    protected void visit(IrLoad irl) {
    }

    protected void visit(IrInvoke invoke) {
    }

    protected void visit(IrLiteral literal) {
    }

    protected void visit(IrReturn irReturn) {
        if (irReturn.exp.isPresent()) {
            visit(irReturn.exp.get());
        }
    }

    protected void visit(IrCast cast) {
    }
}
