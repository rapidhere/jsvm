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
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.LocalVariableNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.TypeInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.common.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * a method node
 *
 * @author rapidhere@gmail.com
 * @version $id: MethodNode.java, v0.1 2017/4/7 dongwei.dq Exp $
 */
public class MethodNode
                       extends
                       CgNode<jdk.internal.org.objectweb.asm.tree.MethodNode, ClassNode, MethodNode> {
    private Map<String, LabelNode>         labels = new HashMap<>();
    private Map<String, LocalVariableNode> locals = new HashMap<>();

    public MethodNode(ClassNode parent, String name) {
        super(parent);
        $.name = name;
    }

    @Override
    protected jdk.internal.org.objectweb.asm.tree.MethodNode constructInnerNode() {
        jdk.internal.org.objectweb.asm.tree.MethodNode inner = new jdk.internal.org.objectweb.asm.tree.MethodNode();
        inner.exceptions = new ArrayList<>();
        inner.localVariables = new ArrayList<>();

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

    public MethodNode stack(int size) {
        $.maxStack = size;
        $.maxLocals = locals.size();
        return this;
    }

    public MethodNode local_var(String name, ClassNode cls, String label1, String label2) {
        LocalVariableNode local = new LocalVariableNode(name, getDescriptor(cls), null,
            labels.get(label1), labels.get(label2), locals.size());

        $.localVariables.add(local);
        locals.put(name, local);
        return this;
    }

    //~ inst goes here

    public MethodNode label(String name) {
        if (!labels.containsKey(name)) {
            LabelNode label = new LabelNode();
            labels.put(name, label);
            $.instructions.add(label);
        } else {
            throw new AssertionError("should label with same name: " + name);
        }

        return this;
    }

    public MethodNode aload(int i) {
        $.instructions.add(new VarInsnNode(Opcodes.ALOAD, i));
        return this;
    }

    public MethodNode dup() {
        $.instructions.add(new InsnNode(Opcodes.DUP));
        return this;
    }

    public MethodNode invoke_static(Class clazz, String methodName, Class... pars) {
        Method method = ReflectionUtil.getMethod(clazz, methodName, pars);
        $.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(clazz),
            method.getName(), Type.getMethodDescriptor(method), false));
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

    public MethodNode store(FieldNode field) {
        $.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, parent.$.name, field.$.name,
            field.$.desc));
        return this;
    }

    public MethodNode load_const(Object o) {
        $.instructions.add(new LdcInsnNode(o));
        return this;
    }

    public MethodNode load_const(int i) {
        int opcode = -1;
        switch (i) {
            case 0:
                opcode = Opcodes.ICONST_0;
                break;
            case 1:
                opcode = Opcodes.ICONST_1;
                break;
            case 2:
                opcode = Opcodes.ICONST_2;
                break;
            case 3:
                opcode = Opcodes.ICONST_3;
                break;
            case 4:
                opcode = Opcodes.ICONST_4;
                break;
            case 5:
                opcode = Opcodes.ICONST_5;
                break;
        }

        if(opcode > 0) {
            $.instructions.add(new InsnNode(opcode));
        } else {
            $.instructions.add(new IntInsnNode(Opcodes.BIPUSH, i));
        }

        return this;
    }

    public MethodNode ret() {
        $.instructions.add(new InsnNode(Opcodes.RETURN));
        return this;
    }
}
