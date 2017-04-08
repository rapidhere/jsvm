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
public class FieldNode extends jdk.internal.org.objectweb.asm.tree.FieldNode {
    private ClassNode                clazz;

    public FieldNode acc(int... accValues) {
        access = 0;
        for (int v : accValues) {
            access += v;
        }
        return this;
    }

    public FieldNode name(String name) {
        this.name = name;
        return this;
    }

    public FieldNode desc(String desc) {
        this.desc = desc;
        return this;
    }

    public FieldNode(ClassNode clazz) {
        super(0, null, null, null, null);
        this.clazz = clazz;
    }

    public ClassNode end() {
        clazz.fields.add(this);
        return clazz;
    }
}
