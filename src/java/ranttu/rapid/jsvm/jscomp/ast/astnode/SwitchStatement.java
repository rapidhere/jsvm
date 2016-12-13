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

import java.util.ArrayList;
import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: SwitchStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class SwitchStatement extends BaseAstNode implements Statement {
    private Expression discriminant;
    private List<SwitchStatement> cases = new ArrayList<>();

    public SwitchStatement(JSONObject jsonObject) {
        super(jsonObject);
        discriminant = Node.of(this, jsonObject, "discriminant");
        jsonObject.getJSONArray("cases").forEach((child) ->
            cases.add(Node.of(this, (JSONObject) child)));
    }

    public Expression getDiscriminant() {
        return discriminant;
    }

    public List<SwitchStatement> getCases() {
        return cases;
    }
}
