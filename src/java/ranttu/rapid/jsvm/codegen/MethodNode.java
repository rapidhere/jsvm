/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.*;
import ranttu.rapid.jsvm.codegen.ir.IrNode;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.MethodConst;
import ranttu.rapid.jsvm.runtime.indy.JsIndyType;

import java.util.*;

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
    public List<String> localVariableNames = new ArrayList<>();
    private Map<String, Type> localVarType = new HashMap<>();

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

    public MethodNode irPrepend(IrNode... ir) {
        List<IrNode> irs = new ArrayList<>();
        Collections.addAll(irs, ir);
        irs.addAll(irNodes);
        irNodes = irs;
        return this;
    }

    public Type getLocalType(String name) {
        return localVarType.get(name);
    }

    public MethodNode ir(IrNode... ir) {
        Collections.addAll(this.irNodes, ir);
        return this;
    }

    public MethodNode pop() {
        $.instructions.add(new InsnNode(Opcodes.POP));
        return this;
    }

    public MethodNode par(String name, Object type, int... acc) {
        int sum = 0;
        for (int a : acc)
            sum += a;

        ParameterNode parNode = new ParameterNode(name, sum);
        $.parameters.add(parNode);
        localVarType.put(name, $$.getType(type));

        return this;
    }

    public MethodNode local(String name, Object type) {
        for (String n : localVariableNames) {
            if (n.equals(name)) {
                return this;
            }
        }
        localVariableNames.add(name);
        localVarType.put(name, $$.getType(type));
        return this;
    }

    //~ inst goes here

    public MethodNode table_switch(int min, int max, LabelNode defaultLabel, LabelNode...labels) {
        $.instructions.add(new TableSwitchInsnNode(
            min, max, defaultLabel, labels));
        return this;
    }

    public MethodNode lookup_switch(LabelNode defaultLabel, Map<Integer, LabelNode> labels) {
        int[] sortedInts = new int[labels.size()];
        LabelNode[] sortedLabels = new LabelNode[labels.size()];

        List<Map.Entry<Integer, LabelNode>> sorted = new ArrayList<>(labels.entrySet());
        sorted.sort(Comparator.comparingInt(Map.Entry::getKey));

        for(int i = 0;i < labels.size();i ++) {
            sortedInts[i] = sorted.get(i).getKey();
            sortedLabels[i] = sorted.get(i).getValue();
        }

        $.instructions.add(new LookupSwitchInsnNode(defaultLabel, sortedInts, sortedLabels));
        return this;
    }

    public MethodNode put_label(LabelNode labelNode) {
        $.instructions.add(labelNode);
        return this;
    }

    public MethodNode fsame() {
        $.instructions.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        return this;
    }

    public MethodNode fsame1(Object name) {
        $.instructions.add(new FrameNode(Opcodes.F_SAME1, 0, null, 0,
            new Object[] { name }));
        return this;
    }

    public MethodNode instance_of(Object name) {
        $.instructions.add(new TypeInsnNode(Opcodes.INSTANCEOF, $$.getInternalName(name)));
        return this;
    }

    public MethodNode ffull(Object[] locals, Object[] stacks) {
        if (stacks == null) {
            stacks = new Object[] {};
        }

        $.instructions.add(new FrameNode(Opcodes.F_FULL, locals.length,
            locals, stacks.length, stacks));
        return this;
    }

    public MethodNode fappend(Object... locals) {
        $.instructions.add(new FrameNode(Opcodes.F_APPEND, locals.length, locals,
            0, null));

        return this;
    }

    public MethodNode add(@SuppressWarnings("unused") Class<? extends Integer> unused) {
        $.instructions.add(new InsnNode(Opcodes.IADD));
        return this;
    }

    public MethodNode load(String name) {
        return load(getLocalNameIndex(name));
    }

    public MethodNode load(int i) {
        Type type = localVarType.get(getLocalName(i));
        if (type == Type.INT_TYPE) {
            $.instructions.add(new VarInsnNode(Opcodes.ILOAD, i));
        } else {
            $.instructions.add(new VarInsnNode(Opcodes.ALOAD, i));
        }

        return this;
    }

    public MethodNode aload(int i) {
        $.instructions.add(new VarInsnNode(Opcodes.ALOAD, i));
        return this;
    }

    public String getLocalName(int idx) {
        if (idx >= $.parameters.size()) {
           return localVariableNames.get(idx - $.parameters.size());
        } else {
            return $.parameters.get(idx).name;
        }
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

    public MethodNode jump_if_acmpne(LabelNode labelNode) {
        $.instructions.add(new JumpInsnNode(Opcodes.IF_ACMPNE, labelNode));
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

    public MethodNode invoke_dynamic(JsIndyType indyType) {
        $.instructions.add(
            new InvokeDynamicInsnNode(indyType.toString(), indyType.getDescriptor(), MethodConst.INDY_JSOBJ_FACTORY));
        return this;
    }

    public MethodNode invoke_dynamic_sam_glue(Class interfaceType) {
        $.instructions.add(
            new InvokeDynamicInsnNode("SAM_GLUE", $$.getMethodDescriptor(interfaceType, Object.class),
                MethodConst.INDY_SAM_GLUE_FACTORY));
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

    public MethodNode invoke_interface(String invokeName, String name, String desc) {
        $.instructions
            .add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, invokeName, name, desc, true));
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

    public MethodNode aastore() {
        $.instructions.add(new InsnNode(Opcodes.AASTORE));
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

    public MethodNode anew_array(String type) {
        $.instructions.add(new TypeInsnNode(Opcodes.ANEWARRAY, type));
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
            $.instructions.add(new LdcInsnNode(i));
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
