/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import jdk.internal.org.objectweb.asm.Opcodes;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
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

    @Override
    protected void on(Program program) {
        // whole module as a top class
        ClassNode cls = newClass()
            .acc(Opcodes.ACC_PUBLIC)
            .name(getClassName(), JsModule.class)
            .source(context.sourceFileName);

        // set this class to the context
        context.moduleClass = cls

        // add MODULE field
        .field()
            .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_FINAL, Opcodes.ACC_STATIC)
            .name(JsModule.FIELD_MODULE_NAME)
            .desc(cls.$.name)
        .end()

        // init MODULE field
        .clinit()
            .new_class(cls.$.name)
            .store_static(cls.last_field())
            .ret()
        .end();
    }

    /**
     * get the top class name of the source file
     * @return the class name
     */
    private String getClassName() {
        // TODO
        // return context.sourceFileName;
        return "TestClass";
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
}
