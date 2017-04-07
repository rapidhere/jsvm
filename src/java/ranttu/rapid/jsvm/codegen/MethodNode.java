/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * a method node
 *
 * @author rapidhere@gmail.com
 * @version $id: MethodNode.java, v0.1 2017/4/7 dongwei.dq Exp $
 */
public class MethodNode {
    private ClassNode                 clazz;
    org.objectweb.asm.tree.MethodNode innerNode;

    public MethodNode(ClassNode clazz) {
        this.clazz = clazz;
        innerNode = new org.objectweb.asm.tree.MethodNode();
    }

    public MethodNode acc(int... accValues) {
        innerNode.access = 0;
        for (int v : accValues) {
            innerNode.access += v;
        }
        return this;
    }

    public MethodNode desc(String desc) {
        innerNode.desc = desc;
        return this;
    }

    public MethodNode name(String name) {
        innerNode.name = name;
        return this;
    }

    public ClassNode end() {
        clazz.innerNode.methods.add(innerNode);
        return clazz;
    }

    //~ inst goes here
    public MethodNode new_class(Class clazz) {
        return new_class(Type.getInternalName(clazz));
    }

    public MethodNode new_class(String internalName) {
        innerNode.instructions.add(new TypeInsnNode(Opcodes.NEW, internalName));
        return this;
    }

    public MethodNode store_static(FieldNode field) {
        innerNode.instructions.add(new FieldInsnNode(Opcodes.PUTSTATIC, clazz.innerNode.name,
            field.innerNode.name, field.innerNode.desc));
        return this;
    }

    public MethodNode ret() {
        innerNode.instructions.add(new InsnNode(Opcodes.RETURN));
        return this;
    }
}
