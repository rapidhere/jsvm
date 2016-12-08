/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Pattern;

/**
 * a identifier
 * @author rapidhere@gmail.com
 * @version $id: Identifier.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class Identifier extends BaseAstNode implements Pattern, Expression {
    /** the name of the identifier */
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
