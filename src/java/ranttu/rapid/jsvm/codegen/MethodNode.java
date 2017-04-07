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
public class MethodNode extends org.objectweb.asm.tree.MethodNode {
    ClassNode clazz;

    public MethodNode(ClassNode clazz) {
        this.clazz = clazz;
    }

    public MethodNode acc(int... accValues) {
        access = 0;
        for (int v : accValues) {
            access += v;
        }
        return this;
    }

    public MethodNode desc(String desc) {
        this.desc = desc;
        return this;
    }

    public MethodNode name(String name) {
        this.name = name;
        return this;
    }

    public ClassNode end() {
        this.clazz.methods.add(this);
        return clazz;
    }

    //~ inst goes here
    public MethodNode new_class(Class clazz) {
        return new_class(Type.getInternalName(clazz));
    }

    public MethodNode new_class(String internalName) {
        instructions.add(new TypeInsnNode(Opcodes.NEW, internalName));
        return this;
    }

    public MethodNode store_static(FieldNode field) {
        instructions.add(new FieldInsnNode(Opcodes.PUTSTATIC, clazz.name, field.name, field.desc));
        return this;
    }

    public MethodNode ret() {
        instructions.add(new InsnNode(Opcodes.RETURN));
        return this;
    }
}
