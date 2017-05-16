/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.ClassWriter;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.ir.*;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.runtime.JsAsyncFunctionObject;
import ranttu.rapid.jsvm.runtime.JsClosure;
import ranttu.rapid.jsvm.runtime.async.FuturePromise;
import ranttu.rapid.jsvm.runtime.async.PromiseResultHandler;
import ranttu.rapid.jsvm.runtime.indy.JsIndyType;

import java.util.HashMap;

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

        // classNode.$.accept(new TraceClassVisitor(new PrintWriter(System.out)));

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.$.accept(cw);
        context.byteCodes.put(classNode.$.name, cw.toByteArray());
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
                .load("reject")
                .swap()
                .load("this")
                .swap()
                .invoke_dynamic(JsIndyType.UNBOUNDED_INVOKE, 1)
                .ret();
        } else {
            method.athrow();
        }
    }

    @Override
    protected void visit(IrLabel label) {
        method.put_label(label.label);
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
                method.invoke_dynamic(JsIndyType.UNBOUNDED_INVOKE, invoke.numberOfArgs);
                break;
            case BOUNDED_FUNC_CALL:
                method.invoke_dynamic(JsIndyType.BOUNDED_INVOKE, invoke.numberOfArgs);
                break;
            case VIRTUAL:
                method.invoke_virtual(invoke.className, invoke.invokeeName, invoke.desc);
                break;
            case CONSTRUCT:
                method.invoke_dynamic(JsIndyType.CONSTRUCT, invoke.numberOfArgs);
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
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrNew irNew) {
        method.new_class(irNew.className);
    }

    @Override
    protected void visit(IrReturn ret) {
        // context in the async function
        if (method.parent.isAsyncFunction && method.$.name.equals("entry")) {
            if (ret.isAwait) {
                // stack: promise
                method
                    .load("this")
                    .swap()
                    .load("closure")
                    .load("accept")
                    .load("reject")
                    .load_const(ret.asyncPoint)
                    .invoke_virtual(
                        $$.getInternalName(JsAsyncFunctionObject.class), "asyncPoint",
                        $$.getMethodDescriptor(void.class, FuturePromise.class, JsClosure.class,
                            PromiseResultHandler.class, PromiseResultHandler.class, int.class))

                    // current method is end, just return
                    .ret()

                    // next entry point
                    .put_label(ret.label.label)

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
                    .load("accept")
                    .swap()
                    .load("this")
                    .swap()
                    .invoke_dynamic(JsIndyType.UNBOUNDED_INVOKE, 1)
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
