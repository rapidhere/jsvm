/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

/**
 * represent a field node
 *
 * @author rapidhere@gmail.com
 * @version $id: FieldNode.java, v0.1 2017/4/7 dongwei.dq Exp $
 */
public class FieldNode extends CgNode<jdk.internal.org.objectweb.asm.tree.FieldNode, ClassNode, FieldNode> {
    public FieldNode(ClassNode parent) {
        super(parent);
    }

    public FieldNode(ClassNode parent, jdk.internal.org.objectweb.asm.tree.FieldNode inner) {
        super(parent, inner);
    }

    @Override
    protected jdk.internal.org.objectweb.asm.tree.FieldNode constructInnerNode() {
        return new jdk.internal.org.objectweb.asm.tree.FieldNode(0, null, null, null, null);
    }

    @Override
    public FieldNode acc(int v) {
        $.access = v;
        return this;
    }

    @Override
    public FieldNode name(String internalName) {
        $.name = internalName;
        return this;
    }

    public FieldNode desc(String desc) {
        $.desc = desc;
        return this;
    }

    public ClassNode end() {
        parent.$.fields.add($);
        return parent;
    }

}
