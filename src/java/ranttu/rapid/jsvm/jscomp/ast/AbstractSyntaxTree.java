/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast;

import ranttu.rapid.jsvm.jscomp.ast.astnode.BaseAstNode;

/**
 * The root of the ast tree
 *
 * @author rapidhere@gmail.com
 * @version $id: AbstractSyntaxTree.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class AbstractSyntaxTree {
    /** the root of the ast*/
    private BaseAstNode root;

    public BaseAstNode getRoot() {
        return root;
    }

    public void setRoot(BaseAstNode root) {
        this.root = root;
    }
}
