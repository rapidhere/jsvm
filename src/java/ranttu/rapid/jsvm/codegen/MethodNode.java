/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import jdk.internal.org.objectweb.asm.tree.InvokeDynamicInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.LocalVariableNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.ParameterNode;
import jdk.internal.org.objectweb.asm.tree.TypeInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;
import ranttu.rapid.jsvm.codegen.ir.IrBlock;
import ranttu.rapid.jsvm.codegen.ir.IrNode;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.runtime.indy.JsIndyType;

import java.util.ArrayList;
import java.util.Collections;
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
    private Map<String, LabelNode>         labels     = new HashMap<>();
    private Map<String, LocalVariableNode> locals     = new HashMap<>();
    private Map<String, ParameterNode>     parameters = new HashMap<>();

    private IrBlock                        root       = IrBlock.of();

    public MethodNode(ClassNode parent, String name) {
        super(parent);
        $.name = name;
    }

    @Override
    protected jdk.internal.org.objectweb.asm.tree.MethodNode constructInnerNode() {
        jdk.internal.org.objectweb.asm.tree.MethodNode inner = new jdk.internal.org.objectweb.asm.tree.MethodNode();
        inner.exceptions = new ArrayList<>();
        inner.localVariables = new ArrayList<>();
        inner.parameters = new ArrayList<>();

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
        $.maxLocals = locals.size() + parameters.size();

        return this;
    }

    public IrBlock ir() {
        return root;
    }

    public MethodNode ir(IrNode... ir) {
        Collections.addAll(this.root.irs, ir);
        return this;
    }

    public MethodNode par(String name, int... acc) {
        int sum = 0;
        for (int a : acc)
            sum += a;

        ParameterNode parNode = new ParameterNode(name, sum);
        parameters.put(name, parNode);
        $.parameters.add(parNode);

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

    public MethodNode add(@SuppressWarnings("unused") Class<? extends Integer> unused) {
        $.instructions.add(new InsnNode(Opcodes.IADD));
        return this;
    }

    public MethodNode sub(@SuppressWarnings("unused") Class<? extends Integer> unused) {
        $.instructions.add(new InsnNode(Opcodes.ISUB));
        return this;
    }

    public MethodNode aload(int i) {
        $.instructions.add(new VarInsnNode(Opcodes.ALOAD, i));
        return this;
    }

    public MethodNode aload(String name) {
        for (int i = 0; i < $.parameters.size(); i++) {
            if ($.parameters.get(i).name.equals(name)) {
                return aload(i);
            }
        }

        for (int i = 0; i < $.localVariables.size(); i++) {
            if ($.localVariables.get(i).name.equals(name)) {
                return aload(i + $.parameters.size());
            }
        }

        return $$.shouldNotReach();
    }

    public MethodNode dup() {
        $.instructions.add(new InsnNode(Opcodes.DUP));
        return this;
    }

    public MethodNode invoke_init(String name, String desc) {
        $.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, name, MethodConst.INIT, desc,
            false));
        return this;
    }

    public MethodNode invoke_dynamic(JsIndyType indyType) {
        $.instructions.add(new InvokeDynamicInsnNode(indyType.toString(), indyType.getDescriptor(),
            MethodConst.INDY_JSOBJ_FACTORY));
        return this;
    }

    public MethodNode invoke_dynamic(JsIndyType indyType, Class...clazz) {
        $.instructions.add(new InvokeDynamicInsnNode(indyType.toString(), indyType.getDescriptor(clazz),
            MethodConst.INDY_JSOBJ_FACTORY));
        return this;
    }

    public MethodNode invoke_special(String invokeName, String name, String desc) {
        $.instructions
            .add(new MethodInsnNode(Opcodes.INVOKESPECIAL, invokeName, name, desc, false));
        return this;
    }

    public MethodNode invoke_virtual(String invokeName, String name, String desc) {
        $.instructions
            .add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, invokeName, name, desc, false));
        return this;
    }

    public MethodNode check_cast(ClassNode cls) {
        return check_cast(cls.$.name);
    }

    public MethodNode check_cast(String internalName) {
        $.instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName));
        return this;
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

    public MethodNode store(String className, String name, String desc) {
        $.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, className, name, desc));
        return this;
    }

    public MethodNode load(String className, String name, String desc) {
        $.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, className, name, desc));
        return this;
    }

    public MethodNode aaload() {
        $.instructions.add(new InsnNode(Opcodes.AALOAD));
        return this;
    }

    public MethodNode load_const(Object o) {
        $.instructions.add(new LdcInsnNode(o));
        return this;
    }

    public MethodNode load_null() {
        $.instructions.add(new InsnNode(Opcodes.ACONST_NULL));
        return this;
    }

    public MethodNode load_const(boolean bool) {
        return load_const(bool ? 1 : 0);
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

        if (opcode > 0) {
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

    public MethodNode aret() {
        $.instructions.add(new InsnNode(Opcodes.ARETURN));
        return this;
    }
}
