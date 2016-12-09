/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

/**
 * a jump label
 *
 * @author rapidhere@gmail.com
 * @version $id: LabeledStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class LabeledStatement extends BaseAstNode implements Statement {
    private Identifier label;
    private Statement body;

    public LabeledStatement(JSONObject jsonObject) {
        super(jsonObject);
        label = new Identifier(jsonObject.getJSONObject("label"));
        body = Node.of(jsonObject, "body");
    }

    public Statement getBody() {
        return body;
    }

    public Identifier getLabel() {
        return label;
    }
}
