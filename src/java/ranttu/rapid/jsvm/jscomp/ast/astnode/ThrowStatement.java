/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

/**
 * a throw statement
 * @author rapidhere@gmail.com
 * @version $id: ThrowStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ThrowStatement extends BaseAstNode implements Statement {
    private Expression argument;

    public ThrowStatement(JSONObject jsonObject) {
        super(jsonObject);
        argument = Node.of(jsonObject, "argument");
    }

    public Expression getArgument() {
        return argument;
    }
}
