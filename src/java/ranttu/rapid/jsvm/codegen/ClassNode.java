/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.common.$;

/**
 * extended asm class node
 *
 * @author rapidhere@gmail.com
 * @version $id: ClassNode.java, v0.1 2017/4/7 dongwei.dq Exp $
 */
public class ClassNode extends jdk.internal.org.objectweb.asm.tree.ClassNode {
    public ClassNode() {
        // TODO: get version from outside
        version = Opcodes.V1_8;
    }

    public ClassNode acc(int... accValues) {
        access = 0;
        for (int v : accValues) {
            access += v;
        }
        return this;
    }

    public ClassNode name(String internal, String superName) {
        name = $.notBlank(internal);

        if (!$.isBlank(superName)) {
            this.superName = superName;
        }

        return this;
    }

    public ClassNode name(String internal, Class superClass) {
        return name(internal, Type.getInternalName(superClass));
    }

    public ClassNode source(String source) {
        sourceFile = source;
        return this;
    }

    public FieldNode field() {
        return new FieldNode(this);
    }

    public MethodNode method() {
        return new MethodNode(this);
    }

    public MethodNode clinit() {
        return method().acc(Opcodes.ACC_STATIC).name("<clinit>")
            .desc(Type.getMethodDescriptor(Type.VOID_TYPE));
    }

    // ~~ some helpers
    public FieldNode last_field() {
        return $.cast(fields.get(fields.size() - 1));
    }
}
