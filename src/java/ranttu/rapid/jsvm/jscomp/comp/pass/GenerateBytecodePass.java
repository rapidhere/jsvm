/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import jdk.internal.org.objectweb.asm.Opcodes;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.FieldNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.jscomp.ast.astnode.AssignmentExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.BinaryExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Literal;
import ranttu.rapid.jsvm.jscomp.ast.astnode.MemberExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ObjectExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Property;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
import ranttu.rapid.jsvm.jscomp.ast.enums.AssignmentOperator;
import ranttu.rapid.jsvm.jscomp.comp.CompilePass;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.runtime.JsNumberObject;
import ranttu.rapid.jsvm.runtime.JsObjectObject;
import ranttu.rapid.jsvm.runtime.JsStringObject;

import java.util.HashMap;
import java.util.Stack;

/**
 * the pass that generate bytecode
 *
 * @author rapidhere@gmail.com
 * @version $id: GenerateBytecodePass.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public class GenerateBytecodePass extends CompilePass {
    /** the class stack*/
    private Stack<ClassNode>  classStack  = new Stack<>();

    /** the method stack */
    private Stack<MethodNode> methodStack = new Stack<>();

    // current clazz in stack
    private ClassNode         clazz;

    // current method in stack
    private MethodNode        method;

    @Override
    protected void beforeProcess() {
        context.moduleClasses = new HashMap<>();
    }

    @Override
    protected void visit(Program program) {
        // whole module as a top class
        ClassNode cls = new ClassNode()
            .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_SUPER)
            .name(context.className, JsModule.class)
            .source(context.sourceFileName);
        context.moduleClasses.put(cls.$.name, cls);

        in(cls).invoke(() -> {
            // add MODULE field
            clazz.field(JsModule.FIELD_MODULE_NAME)
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_FINAL, Opcodes.ACC_STATIC)
                .desc(clazz)
            .end()

            // init MODULE field
            .method_clinit()
                .new_class(clazz)
                .dup()
                .invoke_init(clazz)
                .store_static(clazz.field(JsModule.FIELD_MODULE_NAME))
                .ret()
                .stack(2, 0)
            .end();

            // generate init method
            in(clazz.method_init()).invoke(() -> {
                method
                    .aload(0)
                    .invoke_init(JsModule.class);

                super.visit(program);

                // end init method generate
                method
                    .ret()
                    // TODO
                    .stack(16, 1)
                    .end();
            });
        });
    }

    @Override
    protected void visit(VariableDeclarator variableDeclarator) {
        String varName = variableDeclarator.getId().getName();

        clazz
            .field(varName)
            .acc(Opcodes.ACC_PRIVATE)
            .desc(Object.class);

        if(variableDeclarator.getInitExpression().isPresent()) {
            method.aload(0);
            visit(variableDeclarator.getInitExpression().get());
            method.store(clazz.field(varName));
        }
    }

    @Override
    protected void visit(Literal literal) {
        if (literal.isInt()) {
            method
                .new_class(JsNumberObject.class)
                .dup()
                .load_const(literal.getInt())
                .invoke_init(JsNumberObject.class, int.class);
        } else if (literal.isString()) {
            method
                .new_class(JsStringObject.class)
                .dup()
                .load_const(literal.getString())
                .invoke_init(JsStringObject.class, String.class);
        } else if (literal.isBoolean()) {
            method
                .load_const(literal.getBoolean())
                .invoke_static(Boolean.class, "valueOf", boolean.class);
        } else if (literal.isDouble()) {
            method
                .new_class(JsNumberObject.class)
                .dup()
                .load_const(literal.getDouble())
                .invoke_init(JsNumberObject.class, double.class);
        } else {
            $$.notSupport();
        }
    }

    @Override
    protected void visit(ObjectExpression objExp) {
        ClassNode objClass = clazz.inner_class("Object", JsObjectObject.class, Opcodes.ACC_PRIVATE,
            Opcodes.ACC_SUPER);
        context.moduleClasses.put(objClass.$.name, objClass);

        // load init method
        in(objClass).in(objClass.method_init()).invoke(() -> {
            method
                .aload(0)
                .invoke_init(JsObjectObject.class);

            for (Property prop : objExp.getProperties()) {
                FieldNode field = clazz
                    .field(prop.getKeyString())
                    .acc(Opcodes.ACC_PUBLIC)
                    .desc(Object.class);

                method.aload(0);
                visit(prop.getValue());
                method.store(field);
            }

            // end init method
            method.ret()
            // TODO
                .stack(16, 1);
        });

        // load inner class
        method
            .new_class(objClass)
            .dup()
            .invoke_init(objClass);
    }

    @Override
    protected void visit(AssignmentExpression assignExp) {
        // only support normal assign now
        $$.shouldIn(assignExp.getOperator(), AssignmentOperator.ASSIGN);

        // field assignment
        if (assignExp.getLeft().is(Identifier.class)) {
            method.aload(0);
            visit(assignExp.getRight());
            method.store(clazz.field($$.cast(assignExp.getLeft(), Identifier.class).getName()));
        }
        // common assignment
        else {
            visit(assignExp.getLeft());
            visit(assignExp.getRight());
            method
                .indy_jsobj(MethodConst.SET_PROPERTY, void.class, String.class, Object.class);
        }
    }

    @Override
    protected void visit(MemberExpression memExp) {
        $$.should(! memExp.isComputed());

        //~~~ after visit, the parent object is on the stack
        // for identifier, load the field
        if (memExp.getObject().is(Identifier.class)) {
            method
                .aload(0)
                .load(clazz.field($$.cast(memExp.getObject(), Identifier.class).getName()));
        }
        // others, common visit
        else {
            visit(memExp.getObject());

            //~~~ then, invoke get property
            method
                .indy_jsobj(MethodConst.GET_PROPERTY, Object.class, String.class);
        }

        // put field name const on the stack
        method
            // only un-compiled field is supported
            .load_const($$.cast(memExp.getProperty(), Identifier.class).getName());
    }

    @Override
    protected void visit(BinaryExpression bin) {
        super.visit(bin);

        switch (bin.getOperator()) {
            case ADD:
                method.add(int.class);
                break;
            case SUBTRACT:
                method.sub(int.class);
                break;
            default:
                $$.notSupport();
        }
    }

    // ~~~ invoke helper

    /**
     * do something in the method
     */
    private InvokeWrapper in(MethodNode methodNode) {
        return new InvokeWrapper().in(methodNode);
    }

    /**
     * do something in the class
     */
    private InvokeWrapper in(ClassNode classNode) {
        return new InvokeWrapper().in(classNode);
    }

    /**
     * chain invoker wrapper
     */
    private class InvokeWrapper {
        // the size of method stack before invoke
        private int methodOrigSize;

        // the size of class stack before invoke
        private int classOrigSize;

        public InvokeWrapper() {
            methodOrigSize = methodStack.size();
            classOrigSize = classStack.size();
        }

        /**
         * add a method into stack
         */
        public InvokeWrapper in(MethodNode methodNode) {
            method = methodNode;
            methodStack.push(methodNode);
            return this;
        }

        /**
         * add a class into stack
         */
        public InvokeWrapper in(ClassNode classNode) {
            clazz = classNode;
            classStack.push(classNode);
            return this;
        }

        /**
         * invoke in the context
         */
        public void invoke(Runnable run) {
            run.run();

            // ~~ clear and restore
            method = restore(methodStack, methodOrigSize);
            clazz = restore(classStack, classOrigSize);
        }

        /**
         * restore the stack state after invoke
         */
        private <T> T restore(Stack<T> stack, int sz) {
            while(stack.size() > sz) stack.pop();

            if(! stack.isEmpty()) {
                return stack.peek();
            }

            return null;
        }
    }
}
