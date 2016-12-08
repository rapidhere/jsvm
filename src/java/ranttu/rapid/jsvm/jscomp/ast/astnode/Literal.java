/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

/**
 * @author rapidhere@gmail.com
 * @version $id: Literal.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class Literal<T> extends BaseAstNode {
    private T value;

    public T get() {
        return value;
    }

    public static <T> Literal<T> of(T value) {
        Literal<T> v = new Literal<>();
        v.value = value;
        return v;
    }
}
