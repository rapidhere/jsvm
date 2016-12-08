/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import ranttu.rapid.jsvm.jscomp.ast.asttype.Pattern;

/**
 * a variable declarator
 * @author rapidhere@gmail.com
 * @version $id: VariableDeclarator.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class VariableDeclarator extends BaseAstNode {
    Pattern id;

    public Pattern get() {
        return id;
    }

    public void setId(Pattern id) {
        this.id = id;
    }
}
