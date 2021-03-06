/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.jscomp.comp.CompilingContext;

import java.util.Stack;

/**
 * a compile pass
 *
 * @author rapidhere@gmail.com
 * @version $id: CompilePass.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
abstract public class CompilePass {
    protected CompilingContext context;

    /** the class stack*/
    private Stack<ClassNode>   classStack  = new Stack<>();

    /** the method stack */
    private Stack<MethodNode>  methodStack = new Stack<>();

    // current clazz in stack
    protected ClassNode        clazz;

    // current method in stack
    protected MethodNode       method;

    /**
     * start the compile processing
     */
    final public void process() {
        before();
        start();
        after();
    }

    /**
     * start the compile pass from here
     */
    abstract protected void start();

    // ~~~ hooks
    protected void before() {}

    protected void after() {}

    /**
     * set the compiling context
     *
     * @param compilingContext the context
     */
    final public void setContext(CompilingContext compilingContext) {
        this.context = compilingContext;
    }

    // ~~~ invoke helper

    /**
     * do something in the method
     */
    protected InvokeWrapper in(MethodNode methodNode) {
        return new InvokeWrapper().in(methodNode);
    }

    /**
     * do something in the class
     */
    protected InvokeWrapper in(ClassNode classNode) {
        return new InvokeWrapper().in(classNode);
    }

    /**
     * chain invoker wrapper
     */
    protected class InvokeWrapper {
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
            while (stack.size() > sz)
                stack.pop();

            if (!stack.isEmpty()) {
                return stack.peek();
            }

            return null;
        }
    }
}
