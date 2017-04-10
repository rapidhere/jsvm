/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.exp;

import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * the compile element is not supported yet
 *
 * @author rapidhere@gmail.com
 * @version $id: NotSupportedYet.java, v0.1 2016/12/10 dongwei.dq Exp $
 */
public class NotSupportedYet extends CompileError {
    public NotSupportedYet(Node node) {
        super(node, "not supported yet");
    }

    public NotSupportedYet(Node node, String msg) {
        super(node, "not supported yet: " + msg);
    }
}
