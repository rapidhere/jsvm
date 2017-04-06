/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.bytecode;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * @author rapidhere@gmail.com
 * @version $id: RJvmClass.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public class RJvmClass extends ClassNode {
    public RJvmClass() {
        // TODO: specific a version
        version = Opcodes.V1_8;
    }

    public RJvmClass acc_public() {
        access += Opcodes.ACC_PUBLIC;
        return this;
    }

    public RJvmClass name(@Nonnull String name) {
        this.name = name;
        return this;
    }

    public RJvmClass inherit(@Nonnull RJvmClass superClass) {
        superName = superClass.name;
        return this;
    }

    public RJvmClass inherit(@Nonnull Class clazz) {
        superName = clazz.getName();
        return this;
    }

    public RJvmClass source(@Nonnull String sourceFileName) {
        sourceFile = sourceFileName;
        return this;
    }
}