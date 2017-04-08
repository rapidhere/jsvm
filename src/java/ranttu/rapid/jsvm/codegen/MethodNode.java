/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.TypeInsnNode;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.common.ReflectionUtil;

import java.util.ArrayList;

/**
 * a method node
 *
 * @author rapidhere@gmail.com
 * @version $id: MethodNode.java, v0.1 2017/4/7 dongwei.dq Exp $
 */
public class MethodNode
                       extends
                       CgNode<jdk.internal.org.objectweb.asm.tree.MethodNode, ClassNode, MethodNode> {
    public MethodNode(ClassNode parent) {
        super(parent);
    }

    public MethodNode(ClassNode parent, jdk.internal.org.objectweb.asm.tree.MethodNode inner) {
        super(parent, inner);
    }

    @Override
    protected jdk.internal.org.objectweb.asm.tree.MethodNode constructInnerNode() {
        jdk.internal.org.objectweb.asm.tree.MethodNode inner = new jdk.internal.org.objectweb.asm.tree.MethodNode();
        inner.exceptions = new ArrayList<>();

        return inner;
    }

    @Override
    public MethodNode acc(int v) {
        $.access = v;
        return this;
    }

    @Override
    public MethodNode desc(String desc) {
        $.desc = desc;
        return this;
    }

    @Override
    public MethodNode name(String name) {
        $.name = name;
        return this;
    }

    public MethodNode stack(int size) {
        $.maxStack = size;
        return this;
    }

    public MethodNode locals(int size) {
        $.maxLocals = size;
        return this;
    }

    @Override
    public ClassNode end() {
        parent.$.methods.add($);
        return parent;
    }

    //~ inst goes here
    public MethodNode dup() {
        $.instructions.add(new InsnNode(Opcodes.DUP));
        return this;
    }

    public MethodNode invoke_init(ClassNode clazz) {
        $.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, clazz.$.name,
            MethodConst.INIT, Type.getMethodDescriptor(Type.VOID_TYPE), false));
        return this;
    }

    public MethodNode invoke_init(Class clazz, Class... constructorPars) {
        $.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Type.getInternalName(clazz),
            MethodConst.INIT, Type.getConstructorDescriptor(ReflectionUtil.getConstructor(clazz,
                constructorPars)), false));
        return this;
    }

    public MethodNode new_class(Class clazz) {
        return new_class(Type.getInternalName(clazz));
    }

    public MethodNode new_class(ClassNode classNode) {
        return new_class(classNode.$.name);
    }

    public MethodNode new_class(String internalName) {
        $.instructions.add(new TypeInsnNode(Opcodes.NEW, internalName));
        return this;
    }

    public MethodNode store_static(FieldNode field) {
        $.instructions.add(new FieldInsnNode(Opcodes.PUTSTATIC, parent.$.name, field.$.name,
            field.$.desc));
        return this;
    }

    public MethodNode ret() {
        $.instructions.add(new InsnNode(Opcodes.RETURN));
        return this;
    }
}
