/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.bytecode.RJvmClass;

import java.util.Stack;

/**
 * the compiling context
 *
 * @author rapidhere@gmail.com
 * @version $id: CompilingContext.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public class CompilingContext {
    public Stack<RJvmClass> classStack = new Stack<>();

    public String sourceFileName;

    public RJvmClass currentClass() {
        return classStack.peek();
    }

    public RJvmClass newClass() {
        return classStack.push(new RJvmClass());
    }
}
