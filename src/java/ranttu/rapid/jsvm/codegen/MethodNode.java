/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import ranttu.rapid.jsvm.codegen.ir.IrNode;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.runtime.indy.JsIndyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * a method node
 *
 * @author rapidhere@gmail.com
 * @version $id: MethodNode.java, v0.1 2017/4/7 dongwei.dq Exp $
 */
public class MethodNode
                       extends
                       CgNode<jdk.internal.org.objectweb.asm.tree.MethodNode, ClassNode, MethodNode> {

    private List<IrNode> irNodes            = new ArrayList<>();
    private List<String> localVariableNames = new ArrayList<>();

    public MethodNode(ClassNode parent, String name) {
        super(parent);
        $.name = name;
        $.tryCatchBlocks = new ArrayList<>();
    }

    @Override
    protected jdk.internal.org.objectweb.asm.tree.MethodNode constructInnerNode() {
        jdk.internal.org.objectweb.asm.tree.MethodNode inner = new jdk.internal.org.objectweb.asm.tree.MethodNode();
        inner.exceptions = new ArrayList<>();
        inner.localVariables = new ArrayList<>();
        inner.parameters = new ArrayList<>();

        return inner;
    }

    public MethodNode try_catch(LabelNode start, LabelNode end, LabelNode handler, String excName) {
        $.tryCatchBlocks.add(new TryCatchBlockNode(start, end, handler, excName));
        return this;
    }

    public MethodNode swap() {
        $.instructions.add(new InsnNode(Opcodes.SWAP));
        return this;
    }

    public MethodNode athrow() {
        $.instructions.add(new InsnNode(Opcodes.ATHROW));
        return this;
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

    public List<IrNode> ir() {
        return irNodes;
    }

    public MethodNode ir(IrNode... ir) {
        Collections.addAll(this.irNodes, ir);
        return this;
    }

    public MethodNode par(String name, int... acc) {
        int sum = 0;
        for (int a : acc)
            sum += a;

        ParameterNode parNode = new ParameterNode(name, sum);
        $.parameters.add(parNode);

        return this;
    }

    public MethodNode local(String name) {
        for (String n : localVariableNames) {
            if (n.equals(name)) {
                return this;
            }
        }
        localVariableNames.add(name);
        return this;
    }

    //~ inst goes here

    public MethodNode put_label(LabelNode labelNode) {
        $.instructions.add(labelNode);
        return this;
    }

    public MethodNode add(@SuppressWarnings("unused") Class<? extends Integer> unused) {
        $.instructions.add(new InsnNode(Opcodes.IADD));
        return this;
    }

    public MethodNode aload(int i) {
        $.instructions.add(new VarInsnNode(Opcodes.ALOAD, i));
        return this;
    }

    public MethodNode aload(String name) {
        return aload(getLocalNameIndex(name));
    }

    public int getLocalNameIndex(String name) {
        for (int i = 0; i < $.parameters.size(); i++) {
            if ($.parameters.get(i).name.equals(name)) {
                return i;
            }
        }

        for (int i = 0; i < localVariableNames.size(); i++) {
            if (localVariableNames.get(i).equals(name)) {
                return i + $.parameters.size();
            }
        }

        return $$.shouldNotReach();
    }

    public MethodNode jump(LabelNode label) {
        $.instructions.add(new JumpInsnNode(Opcodes.GOTO, label));
        return this;
    }

    public MethodNode jump_if_eq(LabelNode label) {
        $.instructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
        return this;
    }

    public MethodNode dup() {
        $.instructions.add(new InsnNode(Opcodes.DUP));
        return this;
    }

    public MethodNode invoke_static(String className, String name, String desc) {
        $.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, className, name, desc, false));
        return this;
    }

    public MethodNode invoke_dynamic(JsIndyType indyType, int numberOfArgs) {
        Class[] extraArgs = new Class[numberOfArgs];
        for (int i = 0; i < numberOfArgs; i++) {
            extraArgs[i] = Object.class;
        }

        $.instructions.add(new InvokeDynamicInsnNode(indyType.toString(), indyType
            .getDescriptor(extraArgs), MethodConst.INDY_JSOBJ_FACTORY));
        return this;
    }

    public MethodNode invoke_dynamic(JsIndyType indyType) {
        $.instructions.add(new InvokeDynamicInsnNode(indyType.toString(), indyType.getDescriptor(),
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

    public MethodNode check_cast(String internalName) {
        $.instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, internalName));
        return this;
    }

    public MethodNode new_class(String internalName) {
        $.instructions.add(new TypeInsnNode(Opcodes.NEW, internalName));
        return this;
    }

    public MethodNode store_static(String className, String name, String desc) {
        $.instructions.add(new FieldInsnNode(Opcodes.PUTSTATIC, className, name, desc));
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

    public MethodNode astore(String name) {
        $.instructions.add(new VarInsnNode(Opcodes.ASTORE, getLocalNameIndex(name)));
        return this;
    }

    public MethodNode load_static(String className, String name, String desc) {
        $.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, className, name, desc));
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
