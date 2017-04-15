/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import jdk.internal.org.objectweb.asm.Type;

import static ranttu.rapid.jsvm.common.$$.notSupport;

/**
 * the code gen node
 *
 * @author rapidhere@gmail.com
 * @version $id: AbstractCgNode.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
abstract public class CgNode<T, P extends CgNode, THIS> {
    // the inner one
    public T $;

    // the parent
    public P parent;

    public CgNode(P parent) {
        $ = constructInnerNode();
        this.parent = parent;
    }

    // ~~~ access helpers
    public THIS acc(int... values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }

        return acc(sum);
    }

    public THIS desc(ClassNode classNode) {
        return desc(getDescriptor(classNode));
    }

    public THIS desc(Class clazz) {
        return desc(Type.getDescriptor(clazz));
    }

    public THIS name(String internalName, Class clazz) {
        return name(internalName, Type.getInternalName(clazz));
    }

    // ~~~ abstract interface
    protected abstract T constructInnerNode();

    // ~~~ base accessors
    public THIS acc(int v) {
        return notSupport();
    }

    public THIS name(String internalName, String superName) {
        return notSupport();
    }

    public THIS name(String internalName) {
        return notSupport();
    }

    public THIS desc(String desc) {
        return notSupport();
    }

    public P end() {
        return parent;
    }

    // ~~~ helpers

    public static String getDescriptor(ClassNode classNode) {
        return "L" + classNode.$.name + ";";
    }

    public static Type[] getTypes(Class t1, Class ...types) {
        Type[] ret = new Type[types.length + 1];
        ret[0] = Type.getType(t1);

        for(int i = 0;i < types.length;i ++) {
            ret[i + 1] = Type.getType(types[i]);
        }

        return ret;
    }

    public static Type[] getTypes(Class... types) {
        Type[] ret = new Type[types.length];
        for(int i = 0;i < types.length;i ++) {
            ret[i] = Type.getType(types[i]);
        }

        return ret;
    }
}
