/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.ClassWriter;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.codegen.ir.IrCast;
import ranttu.rapid.jsvm.codegen.ir.IrInvoke;
import ranttu.rapid.jsvm.codegen.ir.IrLiteral;
import ranttu.rapid.jsvm.codegen.ir.IrLoad;
import ranttu.rapid.jsvm.codegen.ir.IrNew;
import ranttu.rapid.jsvm.codegen.ir.IrNode;
import ranttu.rapid.jsvm.codegen.ir.IrReturn;
import ranttu.rapid.jsvm.codegen.ir.IrStore;
import ranttu.rapid.jsvm.codegen.ir.IrThis;
import ranttu.rapid.jsvm.common.$$;
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

        ClassWriter cw = new ClassWriter(0);
        classNode.$.accept(cw);
        context.byteCodes.put(classNode.$.name, cw.toByteArray());
    }

    @Override
    protected void visit(MethodNode methodNode) {
        super.visit(methodNode);

        // TODO: calc stack size and local size
        methodNode.stack(16);
    }

    @Override
    protected void visit(IrInvoke invoke) {
        switch (invoke.type) {
            case SPECIAL:
                // TODO
                String invokeType = $$.cast($$.cast(invoke.invoker, IrLiteral.class).value);
                String invokeName = $$.cast($$.cast(invoke.invokeName, IrLiteral.class).value);
                method.aload("this");
                for (IrNode ir: invoke.args) {
                    visit(ir);
                }
                method.invoke_special(invokeType, invokeName, invoke.desc);
                break;
            case JS_FUNC_CALL:
                visit(invoke.invoker);
                Class clazz[] = new Class[invoke.args.length];

                for(int i = 0;i < invoke.args.length;i ++) {
                    clazz[i] = Object.class;
                    visit(invoke.args[i]);
                }

                method.invoke_dynamic(JsIndyType.INVOKE, clazz);
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrLoad irl) {
        String name;
        switch (irl.type) {
            case FIELD:
                name = $$.cast($$.cast(irl.key, IrLiteral.class).value);
                visit(irl.context);
                method.load(irl.className, name, irl.desc);
                break;
            case PROP:
                visit(irl.context);
                visit(irl.key);
                method.invoke_dynamic(JsIndyType.GET_PROP);
                break;
            case LOCAL:
                name = $$.cast($$.cast(irl.key, IrLiteral.class).value);
                method.aload(name);
                break;
            case ARRAY:
                visit(irl.context);
                int idx = $$.cast($$.cast(irl.key, IrLiteral.class).value);
                method
                    .load_const(idx)
                    .aaload();
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
                method.load_const(literal.getInt());
                break;
            case DOUBLE:
                method.load_const(literal.getDouble());
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrStore irs) {
        String key;

        switch (irs.type) {
            case FIELD:
                visit(irs.context);
                visit(irs.value);
                key = $$.cast($$.cast(irs.key, IrLiteral.class).value);
                method.store(irs.className, key, irs.desc);
                break;
            case PROP:
                visit(irs.context);
                visit(irs.key);
                visit(irs.value);
                method.invoke_dynamic(JsIndyType.SET_PROP);
                break;
            case STATIC_FIELD:
                // key can only be a field
                visit(irs.context);
                visit(irs.value);

                key = $$.cast($$.cast(irs.key, IrLiteral.class).value);
                method.store_static(clazz.field(key));
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrNew irNew) {
        // now only can new a literal
        String name = $$.cast($$.cast(irNew.name, IrLiteral.class).value);

        method.new_class(name).dup();
        for(IrNode o: irNew.args) {
            visit(o);
        }
        method.invoke_init(name, irNew.desc);
    }

    @Override
    protected void visit(IrThis irThis) {
        method.aload("this");
    }

    @Override
    protected void visit(IrReturn ret) {
        if(ret.exp.isPresent()) {
            super.visit(ret);
            method.aret();
        } else {
            method.ret();
        }
    }

    @Override
    protected void visit(IrCast cast) {
        visit(cast.from);
        method.check_cast(cast.name);
    }
}
