/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
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
        ClassNode cls = newClass();

        // set this class to the context
        context.moduleClass = cls;

        // setup class
        cls.access = Opcodes.ACC_PUBLIC;
        cls.name = getClassName();
        cls.superName = JsModule.class.getName();
        cls.sourceFile = context.sourceFileName;

        // add MODULE field
        cls.fields.add(new FieldNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
            JsModule.FIELD_MODULE_NAME, cls.name, "", null));

        // TODO
        // init module field
    }

    /**
     * get the top class name of the source file
     * @return the class name
     */
    private String getClassName() {
        // TODO
        return context.sourceFileName;
    }

    /**
     * create a new class
     * @return the class node
     */
    private ClassNode newClass() {
        ClassNode cls = new ClassNode();
        classStack.push(cls);

        cls.version = Opcodes.V1_8;
        return cls;
    }

    /**
     * get current class
     * @return current class
     */
    private ClassNode clazz() {
        return classStack.peek();
    }
}
