/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.Opcodes;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Literal;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
import ranttu.rapid.jsvm.jscomp.comp.CompilePass;
import ranttu.rapid.jsvm.runtime.JsModule;

import java.util.Stack;

/**
 * the pass that generate bytecode
 *
 * @author rapidhere@gmail.com
 * @version $id: GenerateBytecodePass.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public class GenerateBytecodePass extends CompilePass {
    /** the class stack*/
    private Stack<ClassNode> classStack = new Stack<>();

    /** the method stack */
    private Stack<MethodNode> methodStack = new Stack<>();

    @Override
    protected void visit(Program program) {
        // whole module as a top class
        ClassNode cls = newClass()
            .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_SUPER)
            .name(context.className, JsModule.class)
            .source(context.sourceFileName);

        // set this class to the context
        context.moduleClass = cls

        // add MODULE field
        .field(JsModule.FIELD_MODULE_NAME)
            .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_FINAL, Opcodes.ACC_STATIC)
            .desc(cls)
        .end()

        // init MODULE field
        .method_clinit()
            .new_class(cls)
            .dup()
            .invoke_init(cls)
            .store_static(cls.field(JsModule.FIELD_MODULE_NAME))
            .ret()
            .stack(2)
        .end();

        // generate init method
        cls.method_init()
            .label("L0")
            .aload(0)
            .invoke_init(JsModule.class);

        super.visit(program);

        // end init method generate
        cls.method_init()
            .label("L1")
            .ret()
            .local_var("this", cls, "L0", "L1")
            .stack(2)
        .end();
    }

    @Override
    protected void visit(VariableDeclarator variableDeclarator) {
        String varName = variableDeclarator.getId().getName();

        clazz().field(varName)
            .acc(Opcodes.ACC_PRIVATE)
            .desc(Object.class);

        methodStack.push(clazz().method_init());
        method().aload(0);
        super.visit(variableDeclarator);
        method().store(clazz().field(varName));
        methodStack.pop();
    }

    @Override
    protected void visit(Literal literal) {
        if (literal.isInt()) {
            method().load_const(literal.getInt())
                .invoke_static(Integer.class, "valueOf", int.class);
        } else if (literal.isString()) {
            method().load_const(literal.getString());
        } else {
            $$.notSupport();
        }
    }

    /**
     * create a new class
     * @return the class node
     */
    private ClassNode newClass() {
        return classStack.push(new ClassNode());
    }

    /**
     * get current class
     * @return current class
     */
    private ClassNode clazz() {
        return classStack.peek();
    }

    /**
     * get current method
     * @return current method
     */
    private MethodNode method() {
        return methodStack.peek();
    }
}
