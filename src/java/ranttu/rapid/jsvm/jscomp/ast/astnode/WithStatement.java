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
 * a with statement
 *
 * @author rapidhere@gmail.com
 * @version $id: WithStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class WithStatement extends BaseAstNode implements Statement {
    private Expression object;
    private Statement body;

    public WithStatement(JSONObject jsonObject) {
        super(jsonObject);
        object = Node.of(jsonObject, "object");
        body = Node.of(jsonObject, "body");
    }

    public Expression getObject() {
        return object;
    }

    public Statement getBody() {
        return body;
    }
}
