/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.InnerClassNode;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.MethodConst;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ranttu.rapid.jsvm.common.$$.isBlank;
import static ranttu.rapid.jsvm.common.$$.notBlank;

/**
 * extended asm class node
 *
 * @author rapidhere@gmail.com
 * @version $id: ClassNode.java, v0.1 2017/4/7 dongwei.dq Exp $
 */
public class ClassNode extends
                      CgNode<jdk.internal.org.objectweb.asm.tree.ClassNode, ClassNode, ClassNode> {
    private Map<String, FieldNode>  fields  = new HashMap<>();
    private Map<String, MethodNode> methods = new HashMap<>();
    private Map<String, ClassNode>  innerClasses = new HashMap<>();

    public ClassNode() {
        super(null);
    }

    @Override
    protected jdk.internal.org.objectweb.asm.tree.ClassNode constructInnerNode() {
        jdk.internal.org.objectweb.asm.tree.ClassNode inner = new jdk.internal.org.objectweb.asm.tree.ClassNode();
        // TODO: get version from outside
        inner.version = Opcodes.V1_8;

        return inner;
    }

    // ~~~ public access method
    @Override
    public ClassNode acc(int v) {
        $.access = v;
        return this;
    }

    @Override
    public ClassNode name(String internal, String superName) {
        $.name = notBlank(internal);

        if (!isBlank(superName)) {
            $.superName = superName;
        }

        return this;
    }

    @Override
    public ClassNode end() {
        return $$.notSupport();
    }

    // ~~~ inner classes
    public ClassNode inner_class(String innerName, Class superClass, int...acces) {
        return inner_class(innerName, Type.getInternalName(superClass), acces);
    }

    public Collection<ClassNode> innerClasses() {
        return innerClasses.values();
    }

    public ClassNode inner_class(String innerName, String superName, int...acces) {
        ClassNode cls = new ClassNode()
            .name($.name + "$" + innerName +
                ($.innerClasses.size() + 1), superName)
            .acc(acces);
        $.innerClasses.add(
            new InnerClassNode(cls.$.name, $.name, innerName, cls.$.access));
        innerClasses.put(cls.$.name, cls);

        return cls;
    }

    // ~~~ class node specified access
    public ClassNode source(String source) {
        $.sourceFile = source;
        return this;
    }

    // ~~~ fields
    public FieldNode field(String name) {
        if (!fields.containsKey(name)) {
            FieldNode fieldNode = new FieldNode(this, name);

            fields.put(name, fieldNode);
            $.fields.add(fieldNode.$);
        }
        return fields.get(name);
    }

    // ~~~ methods
    public Collection<MethodNode> methods() {
        return methods.values();
    }

    public MethodNode method(String name) {
        if (!methods.containsKey(name)) {
            MethodNode methodNode = new MethodNode(this, name);
            methods.put(name, methodNode);
            $.methods.add(methodNode.$);
        }

        return methods.get(name);
    }

    public MethodNode method_clinit() {
        return method(MethodConst.CLINIT).acc(Opcodes.ACC_STATIC).desc(
            Type.getMethodDescriptor(Type.VOID_TYPE));
    }

    public MethodNode method_init() {
        return method(MethodConst.INIT).desc(Type.getMethodDescriptor(Type.VOID_TYPE));
    }
}
