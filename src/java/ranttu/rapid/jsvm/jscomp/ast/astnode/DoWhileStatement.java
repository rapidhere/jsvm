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
 * a do while loop
 * @author rapidhere@gmail.com
 * @version $id: DoWhileStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class DoWhileStatement extends BaseAstNode implements Statement {
    private Expression test;
    private Statement body;

    public DoWhileStatement(JSONObject jsonObject) {
        super(jsonObject);
        test = Node.of(this, jsonObject, "test");
        body = Node.of(this, jsonObject, "body");
    }

    public Statement getBody() {
        return body;
    }

    public Expression getTest() {
        return test;
    }
}
