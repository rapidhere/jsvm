/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import ranttu.rapid.jsvm.common.MethodConst;

import static ranttu.rapid.jsvm.common.$$.isBlank;
import static ranttu.rapid.jsvm.common.$$.notBlank;

/**
 * extended asm class node
 *
 * @author rapidhere@gmail.com
 * @version $id: ClassNode.java, v0.1 2017/4/7 dongwei.dq Exp $
 */
public class ClassNode extends CgNode<jdk.internal.org.objectweb.asm.tree.ClassNode, ClassNode, ClassNode> {
    public ClassNode() {
        super(null);
    }

    @Override
    protected jdk.internal.org.objectweb.asm.tree.ClassNode constructInnerNode() {
        jdk.internal.org.objectweb.asm.tree.ClassNode inner = new jdk.internal.org.objectweb.asm.tree.ClassNode();
        // TODO: get version from outside
        inner.version = Opcodes.V1_8;

        return inner;
    }

    // ~~~ public access method
    @Override
    public ClassNode acc(int v) {
        $.access = v;
        return this;
    }

    @Override
    public ClassNode name(String internal, String superName) {
        $.name = notBlank(internal);

        if (!isBlank(superName)) {
            $.superName = superName;
        }

        return this;
    }

    // ~~~ class node specified access
    public ClassNode source(String source) {
        $.sourceFile = source;
        return this;
    }

    // ~~~ fields
    public FieldNode field() {
        return new FieldNode(this);
    }

    // ~~~ methods
    public MethodNode method() {
        return new MethodNode(this);
    }

    public MethodNode method_clinit() {
        return method().acc(Opcodes.ACC_STATIC).name(MethodConst.CLINIT)
            .desc(Type.getMethodDescriptor(Type.VOID_TYPE));
    }

    public MethodNode method_init() {
        return method().name(MethodConst.INIT)
            .desc(Type.getMethodDescriptor(Type.VOID_TYPE));
    }

    // ~~ some helpers
    public FieldNode last_field() {
        return new FieldNode(this, $.fields.get($.fields.size() - 1));
    }
}
