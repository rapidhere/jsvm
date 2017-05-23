/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.ir.*;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.runtime.JsAsyncFunctionObject;
import ranttu.rapid.jsvm.runtime.JsClosure;
import ranttu.rapid.jsvm.runtime.async.FuturePromise;
import ranttu.rapid.jsvm.runtime.async.PromiseResultHandler;
import ranttu.rapid.jsvm.runtime.indy.JsIndyType;

import java.util.HashMap;
import java.util.List;

/**
 * the pass that generate bytecode
 *
 * @author rapidhere@gmail.com
 * @version $id: GenerateBytecodePass.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public class GenerateBytecodePass extends IrBasedCompilePass {
    @Override
    protected void before() {
        context.byteCodes = new HashMap<>();
    }

    @Override
    protected void visit(ClassNode classNode) {
        super.visit(classNode);

        context.byteCodes.put(classNode.$.name, classNode.writeClass(1));
    }

    @Override
    protected void visit(IrSwitch irSwitch) {
        method.table_switch(
            irSwitch.min, irSwitch.max, irSwitch.defaultLabel, irSwitch.labels);
    }

    @Override
    protected void visit(IrThrow irThrow) {
        if (method.parent.isAsyncFunction && method.$.name.equals("entry")) {
            // see method ret has value
            method
                .local("__args__", Object[].class)
                .load_const(1)
                .anew_array($$.getInternalName(Object.class))
                .dup()
                .astore("__args__")
                .swap()
                .load_const(0)
                .swap()
                .aastore()
                .load("reject")
                .load("this")
                .load("__args__")
                .invoke_dynamic(JsIndyType.UNBOUNDED_INVOKE)
                .ret();
        } else {
            method.athrow();
        }
    }

    private void resolveFrame(IrLabel label) {
        if (label.needFrame) {
            boolean exs = label.extraStacks != null && ! label.extraStacks.isEmpty();
            boolean exl = label.extraLocals != null && ! label.extraLocals.isEmpty();
            // resolve frame type
            if (exs && exl || label.useFull) {
                method.ffull(resolveFrameString(label.extraLocals),
                    resolveFrameString(label.extraStacks));
            } else if (exl && !exs) {
                method.fappend(resolveFrameString(label.extraLocals));
            } else if (!exl && exs) {
                method.fsame1(resolveFrameString(label.extraStacks)[0]);
            } else {
                method.fsame();
            }
        }
    }

    private Object[] resolveFrameString(List<Type> types) {
        Object[] s = new Object[types.size()];
        for(int i = 0;i < types.size();i ++) {
            Type t = types.get(i);
            try {
                s[i] = t.getInternalName();
            } catch (NullPointerException e) {
                // TODO
                s[i] = Opcodes.INTEGER;
            }
        }

        return s;
    }

    @Override
    protected void visit(IrLabel label) {
        method.put_label(label.label);
        resolveFrame(label);
    }

    @Override
    protected void visit(IrJump irJump) {
        switch (irJump.type) {
            case DIRECT:
                method.jump(irJump.label);
                break;
            case IF_EQ:
                method.jump_if_eq(irJump.label);
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrInvoke invoke) {
        switch (invoke.type) {
            case STATIC:
                method.invoke_static(invoke.className, invoke.invokeeName, invoke.desc);
                break;
            case SPECIAL:
                method.invoke_special(invoke.className, invoke.invokeeName, invoke.desc);
                break;
            case UNBOUNDED_FUNC_CALL:
                method.invoke_dynamic(JsIndyType.UNBOUNDED_INVOKE);
                break;
            case BOUNDED_FUNC_CALL:
                method.invoke_dynamic(JsIndyType.BOUNDED_INVOKE);
                break;
            case VIRTUAL:
                method.invoke_virtual(invoke.className, invoke.invokeeName, invoke.desc);
                break;
            case CONSTRUCT:
                method.invoke_dynamic(JsIndyType.CONSTRUCT);
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrSwap swap) {
        method.swap();
    }

    @Override
    protected void visit(IrDup dup) {
        method.dup();
    }

    @Override
    protected void visit(IrLoad irl) {
        switch (irl.type) {
            case FIELD:
                method.load(irl.className, irl.key, irl.desc);
                break;
            case PROP:
                method.invoke_dynamic(JsIndyType.GET_PROP);
                break;
            case LOCAL:
                method.load(irl.key);
                break;
            case ARRAY:
                method.load_const(irl.index).aaload();
                break;
            case STATIC_FIELD:
                method.load_static(irl.className, irl.key, irl.desc);
                break;
            case CONST:
                method.load_const(irl.constVal);
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrLiteral literal) {
        switch (literal.type) {
            case STRING:
                method.load_const(literal.getString());
                break;
            case INTEGER:
                method.load_const(literal.getInt()).invoke_static(
                    $$.getInternalName(Integer.class), "valueOf",
                    $$.getMethodDescriptor(Integer.class, int.class));
                break;
            case DOUBLE:
                method.load_const(literal.getDouble()).invoke_static(
                    $$.getInternalName(Double.class), "valueOf",
                    $$.getMethodDescriptor(Double.class, double.class));
                break;
            case BOOLEAN:
                method.load_static($$.getInternalName(Boolean.class),
                    literal.getBoolean() ? "TRUE" : "FALSE", $$.getDescriptor(Boolean.class));
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrStore irs) {
        switch (irs.type) {
            case FIELD:
                method.store(irs.className, irs.key, irs.desc);
                break;
            case PROP:
                method.invoke_dynamic(JsIndyType.SET_PROP);
                break;
            case STATIC_FIELD:
                method.store_static(irs.className, irs.key, irs.desc);
                break;
            case LOCAL:
                method.astore(irs.key);
                break;
            case ARRAY:
                method.aastore();
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrNew irNew) {
        if (irNew.isArray) {
            method
                .load_const(irNew.size)
                .anew_array(irNew.className);
        } else {
            method.new_class(irNew.className);
        }
    }

    @Override
    protected void visit(IrReturn ret) {
        // context in the async function
        if (method.parent.isAsyncFunction && method.$.name.equals("entry")) {
            if (ret.isAwait) {
                int stackSize = ret.restStack.size();

                // store the __promise__ object
                method
                    .local("__promise__", FuturePromise.class)
                    .local("__stack__", Object[].class)
                    .astore("__promise__")

                // store the stack in reversed order
                    .load_const(stackSize)
                    .anew_array($$.getInternalName(Object.class))
                    .astore("__stack__");
                for(int i = 0;i < stackSize;i ++) {
                    Type t = Type.getType(ret.restStack.get(stackSize - 1 - i));
                    method
                        .load("__stack__")
                        .swap()
                        .load_const(i)
                        .swap();

                    if (t == Type.INT_TYPE) {
                        method.invoke_static($$.getInternalName(Integer.class), "valueOf",
                            $$.getMethodDescriptor(Integer.class, int.class));
                    }
                    method.aastore();
                }

                method
                // call the async point
                    .load("this")
                    .load("__stack__")
                    .load("closure")
                    .load("__promise__")
                    .load("accept")
                    .load("reject")
                    .load_const(ret.asyncPoint)
                    .invoke_virtual(
                        $$.getInternalName(JsAsyncFunctionObject.class), "asyncPoint",
                        $$.getMethodDescriptor(void.class, Object[].class, JsClosure.class, FuturePromise.class,
                            PromiseResultHandler.class, PromiseResultHandler.class, int.class))

                // current method is end, just return
                    .ret()

                // next entry point
                    .put_label(ret.label.label);
                // resolve frame
                resolveFrame(ret.label);

                // restore stack
                for (int i = stackSize - 1;i >= 0;i --) {
                    method
                        .load("stack")
                        .load_const(i)
                        .aaload();
                    Type t = Type.getType(ret.restStack.get(stackSize - 1 - i));

                    // TODO
                    if (t == Type.INT_TYPE) {
                        method
                            .check_cast($$.getInternalName(Integer.class))
                            .invoke_virtual($$.getInternalName(Integer.class), "intValue",
                                $$.getMethodDescriptor(int.class));
                    } else {
                        method.check_cast(t.getInternalName());
                    }
                }

                method
                // load error and result
                    .load("error")
                    .load("result")
                    .invoke_static($$.getInternalName(JsAsyncFunctionObject.class), "getResultOrThrow",
                        $$.getMethodDescriptor(Object.class, Object.class, Object.class))
                    ;

            } else if(ret.hasReturnValue) {
                // ret_val is on stack
                // ret_val -> ret_val, accept -> accept, ret_val
                // -> accept, ret_val, this
                // -> accept, this, ret_val
                method
                    .local("__args__", Object[].class)
                    .load_const(1)
                    .anew_array($$.getInternalName(Object.class))
                    .dup()
                    .astore("__args__")
                    .swap()
                    .load_const(0)
                    .swap()
                    .aastore()
                    .load("accept")
                    .load("this")
                    .load("__args__")
                    .invoke_dynamic(JsIndyType.UNBOUNDED_INVOKE)
                    .ret();
            } else {
                $$.notSupport();
            }
        } else {
            if (ret.hasReturnValue) {
                method.aret();
            } else {
                method.ret();
            }
        }
    }

    @Override
    protected void visit(IrCast cast) {
        method.check_cast(cast.name);
    }
}
