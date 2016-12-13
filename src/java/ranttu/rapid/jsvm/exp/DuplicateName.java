/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.exp;

import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * name is duplicated
 *
 * @author rapidhere@gmail.com
 * @version $id: DuplicateName.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
public class DuplicateName extends CompileError {
    public DuplicateName(Node node, String id) {
        super(node, "duplicate name in: " + id);
    }
}
