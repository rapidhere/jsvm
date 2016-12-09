/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;
import ranttu.rapid.jsvm.jscomp.ast.asttype.TopLevelNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Top Level Script
 * @author rapidhere@gmail.com
 * @version $id: Script.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class Script extends BaseAstNode implements TopLevelNode {
    /** the body of the program */
    private List<Statement> body = new ArrayList<>();

    public Script(JSONObject jsonObject) {
        super(jsonObject);

        jsonObject.getJSONArray("body").forEach((child) ->
            body.add(Node.of((JSONObject) child)));
    }

    public List<Statement> getBody() {
        return body;
    }
}
