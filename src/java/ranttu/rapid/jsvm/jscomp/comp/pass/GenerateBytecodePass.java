/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.codegen.ir.FieldType;
import ranttu.rapid.jsvm.codegen.ir.InvokeType;
import ranttu.rapid.jsvm.codegen.ir.IrInvoke;
import ranttu.rapid.jsvm.codegen.ir.IrLiteral;
import ranttu.rapid.jsvm.codegen.ir.IrLoad;
import ranttu.rapid.jsvm.codegen.ir.IrNew;
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
        $$.shouldIn(invoke.type, InvokeType.SPECIAL);

        // TODO
        String invokeType = $$.cast($$.cast(invoke.invoker, IrLiteral.class).value);
        String invokeName = $$.cast($$.cast(invoke.invokeName, IrLiteral.class).value);
        method.aload("this")
            .invoke_special(invokeType, invokeName, Type.getMethodDescriptor(Type.VOID_TYPE));
    }

    @Override
    protected void visit(IrLoad irl) {
        $$.shouldIn(irl.type, FieldType.FIELD);

        visit(irl.context);
        visit(irl.key);
        method.invoke_dynamic(JsIndyType.GET_PROP);
    }

    @Override
    protected void visit(IrLiteral literal) {
        switch (literal.type) {
            case STRING:
                method.load_const(literal.getString());
                break;
            default:
                $$.notSupport();
        }
    }

    @Override
    protected void visit(IrStore irs) {
        switch (irs.type) {
            case FIELD:
                visit(irs.context);
                visit(irs.key);
                visit(irs.value);
                method.invoke_dynamic(JsIndyType.SET_PROP);
                break;
            case STATIC_FIELD:
                // key can only be a field
                visit(irs.context);
                visit(irs.value);

                String key = $$.cast($$.cast(irs.key, IrLiteral.class).value);
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

        for(Object o: irNew.args) {
            method.load_const(o);
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
}
