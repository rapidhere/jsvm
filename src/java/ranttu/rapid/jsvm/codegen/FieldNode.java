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
public class FieldNode {
    private ClassNode                clazz;
    org.objectweb.asm.tree.FieldNode innerNode;

    public FieldNode acc(int... accValues) {
        innerNode.access = 0;
        for (int v : accValues) {
            innerNode.access += v;
        }
        return this;
    }

    public FieldNode name(String name) {
        innerNode.name = name;
        return this;
    }

    public FieldNode desc(String desc) {
        innerNode.desc = desc;
        return this;
    }

    public FieldNode(ClassNode clazz) {
        innerNode = new org.objectweb.asm.tree.FieldNode(0, null, null, null, null);
        this.clazz = clazz;
    }

    public ClassNode end() {
        clazz.innerNode.fields.add(innerNode);
        clazz.lastField = this;
        return clazz;
    }
}
