/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.exp;

import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * @author rapidhere@gmail.com
 * @version $id: NoSuchName.java, v0.1 2017/4/19 dongwei.dq Exp $
 */
public class NoSuchName extends CompileError {
    public NoSuchName(Node node, String id) {
        super(node, "no such name in: " + id);
    }
}
