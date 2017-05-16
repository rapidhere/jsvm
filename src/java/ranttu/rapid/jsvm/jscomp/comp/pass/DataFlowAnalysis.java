package ranttu.rapid.jsvm.jscomp.comp.pass;

import com.google.common.collect.ImmutableList;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.codegen.ir.*;
import ranttu.rapid.jsvm.common.$$;

import java.util.*;

/**
 * a compile pass that analysis the data flow, for:
 *
 * compute frame, async stack storage
 *
 * @author rapidhere@gmail.com
 * @version $id: DataFlowAnalysis.java, v0.1 2017/4/19 dongwei.dq Exp $
 */
public class DataFlowAnalysis extends IrBasedCompilePass {
    private Stack<String> stackFrame;

    private Map<LabelNode, List<String>> frameInfo;

    private boolean frameUpdatedInTurn = false;

    private boolean frameComputed = false;

    @Override
    public void visit(ClassNode clazz) {
        try {
            super.visit(clazz);
        } catch (Throwable e) {
            System.err.print("wrong in data flow analysis, escaped");
            e.printStackTrace();
        }
    }

    @Override
    public void visit(MethodNode methodNode) {
        stackFrame = new Stack<>();
        frameInfo = new HashMap<>();
        frameComputed = false;

        // compute frame
        do {
            frameUpdatedInTurn = false;
            super.visit(methodNode);
            stackFrame.clear();
        } while (frameUpdatedInTurn);

        // now insert frame
        frameComputed = true;
        super.visit(methodNode);
    }

    @Override
    protected void visit(IrSwitch irSwitch) {
        // pop switch operator number
        stackFrame.pop();

        mergeFrame(irSwitch.defaultLabel);
        for(LabelNode labelNode: irSwitch.labels) {
            mergeFrame(labelNode);
        }
    }

    @Override
    protected void visit(IrThrow irThrow) {
        // TODO, not supported now
    }

    @Override
    protected void visit(IrSwap swap) {
        String first = stackFrame.pop(),
            second = stackFrame.pop();
        stackFrame.push(second);
        stackFrame.push(first);
    }

    @Override
    protected void visit(IrLabel irLabel) {
        mergeFrame(irLabel.label);
    }

    @Override
    protected void visit(IrDup irDup) {
        String first = stackFrame.pop();
        stackFrame.push(first);
        stackFrame.push(first);
    }

    @Override
    protected void visit(IrNew irNew) {
        stackFrame.push($$.getDescriptor(irNew.className));
    }

    @Override
    protected void visit(IrStore irs) {
        switch (irs.type) {
            case PROP:
                stackFrame.pop();
            case FIELD:
                stackFrame.pop();
            case LOCAL:
            case STATIC_FIELD:
                stackFrame.pop();
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrInvoke invoke) {
        String desc = invoke.desc;
        switch (invoke.type) {
            case STATIC:
                computeFrame(retDesc(desc), argSize(desc));
                break;
            case SPECIAL:
            case VIRTUAL:
                computeFrame(retDesc(desc), argSize(desc) + 1);
                break;
            case BOUNDED_FUNC_CALL:
                computeFrame($$.getDescriptor(Object.class), invoke.numberOfArgs + 2);
                break;
            case UNBOUNDED_FUNC_CALL:
                computeFrame($$.getDescriptor(Object.class), invoke.numberOfArgs + 2);
                break;
            case CONSTRUCT:
                computeFrame($$.getDescriptor(Object.class), invoke.numberOfArgs + 1);
                break;
            default:
                $$.notSupport();
        }
    }

    private void computeFrame(String retClazz, int argumentSize) {
        for (int i = 0;i < argumentSize;i ++) {
            stackFrame.pop();
        }

        if (retClazz != null) {
            stackFrame.push(retClazz);
        }
    }

    private int argSize(String desc) {
        return Type.getArgumentTypes(desc).length;
    }

    private String retDesc(String desc) {
        if (Type.getReturnType(desc) == Type.VOID_TYPE) {
           return null;
        } else {
            return Type.getReturnType(desc).getDescriptor();
        }
    }

    @Override
    protected void visit(IrLiteral literal) {
        switch (literal.type) {
            case STRING:
                stackFrame.push($$.getDescriptor(String.class));
                break;
            case INTEGER:
                stackFrame.push($$.getDescriptor(Integer.class));
                break;
            case DOUBLE:
                stackFrame.push($$.getDescriptor(Double.class));
                break;
            case BOOLEAN:
                stackFrame.push($$.getDescriptor(Boolean.class));
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrReturn irReturn) {
        if (frameComputed) {
            // for await node store the stack
            if (irReturn.isAwait) {
                // remove the top future promise object
                List<String> stackTypes = ImmutableList.copyOf(stackFrame);
                irReturn.restStack = stackTypes.subList(0, stackTypes.size() - 1);
            }
        }

        if (irReturn.isAwait) {
            stackFrame.pop();
            stackFrame.push($$.getDescriptor(Object.class));
        } else {
            stackFrame.clear();
        }
    }

    @Override
    protected void visit(IrCast cast) {
        stackFrame.pop();
        stackFrame.push($$.getDescriptor(cast.name));
    }

    @Override
    public void visit(IrLoad irl) {
        switch (irl.type) {
            case FIELD:
                stackFrame.pop();
                stackFrame.push(irl.desc);
                break;
            case PROP:
                stackFrame.pop();
                stackFrame.pop();
                stackFrame.push($$.getDescriptor(Object.class));
                break;
            case LOCAL:
                stackFrame.push(method.getLocalType(irl.key).getDescriptor());
                break;
            case ARRAY:
                stackFrame.pop();
                stackFrame.push($$.getDescriptor(Object.class));
                break;
            case STATIC_FIELD:
                stackFrame.push(irl.desc);
                break;
            case CONST:
                stackFrame.push($$.getDescriptor(irl.constVal.getClass()));
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    public void visit(IrJump jump) {
        switch (jump.type) {
            case IF_EQ:
                // remove top
                stackFrame.pop();
        }

        // compute frame
        mergeFrame(jump.label);
    }

    private void mergeFrame(LabelNode label) {
        List<String> frame = frameInfo.get(label);

        // first of frame
        if (frame == null) {
           frame = new ArrayList<>(stackFrame);
           // this frame is updated
           frameUpdatedInTurn = true;
           frameInfo.put(label, frame);
        } else if(! frame.equals(stackFrame)) {
            throw new RuntimeException(
                "stack frame compared not successfully " + label);
        }
    }
}
