package ranttu.rapid.jsvm.jscomp.comp.pass;

import com.google.common.collect.ImmutableList;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.ParameterNode;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.codegen.ir.*;
import ranttu.rapid.jsvm.common.$$;

import java.util.Stack;

/**
 * a compile pass that analysis the data flow, for:
 *
 * compute frame, async stack storage, local var append, reach analysis
 *
 * @author rapidhere@gmail.com
 * @version $id: ControlFlowAnalysis.java, v0.1 2017/4/19 dongwei.dq Exp $
 */
public class ControlFlowAnalysis extends IrBasedCompilePass {
    private Stack<String> stackFrame;
    private boolean localsGenerated = false;

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
        localsGenerated = false;

        super.visit(methodNode);
    }

    @Override
    protected void visit(IrSwitch irSwitch) {
        // pop switch operator number
        stackFrame.pop();
    }

    @Override
    protected void visit(IrThrow irThrow) {
        stackFrame.clear();
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
        // append stacks
        if (irLabel.extraStacks != null) {
           for (Type type: irLabel.extraStacks) {
               stackFrame.push(type.getDescriptor());
           }
        }

        // generate locals
        if (irLabel.needFrame && ! localsGenerated) {
            localsGenerated = true;

            for(ParameterNode par: method.$.parameters) {
                irLabel.extraLocals.add(method.getLocalType(par.name));
            }
            for(String name: method.localVariableNames) {
                irLabel.extraLocals.add(method.getLocalType(name));
            }

            irLabel.full();
        }
    }

    @Override
    protected void visit(IrDup irDup) {
        String first = stackFrame.pop();
        stackFrame.push(first);
        stackFrame.push(first);
    }

    @Override
    protected void visit(IrNew irNew) {
        if (irNew.isArray) {
            stackFrame.push("[" + $$.getDescriptor(irNew.className));
        } else {
            stackFrame.push($$.getDescriptor(irNew.className));
        }
    }

    @Override
    protected void visit(IrStore irs) {
        switch (irs.type) {
            case PROP:
            case ARRAY:
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
                computeFrame($$.getDescriptor(Object.class), 3);
                break;
            case UNBOUNDED_FUNC_CALL:
                computeFrame($$.getDescriptor(Object.class), 3);
                break;
            case CONSTRUCT:
                computeFrame($$.getDescriptor(Object.class), 2);
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
        if (irReturn.isAwait) {
            stackFrame.pop();
            // compute async stack
            irReturn.restStack = ImmutableList.copyOf(stackFrame);
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
                if (irl.constVal instanceof Integer) {
                    stackFrame.push(Type.INT_TYPE.getDescriptor());
                } else {
                    stackFrame.push($$.getDescriptor(irl.constVal.getClass()));
                }
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
    }
}
