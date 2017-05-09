/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import ranttu.rapid.jsvm.common.$$;

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
        return desc($$.getDescriptor(classNode));
    }

    public THIS desc(Class clazz) {
        return desc($$.getDescriptor(clazz));
    }

    public THIS desc(Class retType, Class... pars) {
        return desc($$.getMethodDescriptor(retType, pars));
    }

    public THIS name(String internalName, Class clazz) {
        return name(internalName, $$.getInternalName(clazz));
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

    public THIS desc(String desc) {
        return notSupport();
    }

    public P end() {
        return parent;
    }
}
