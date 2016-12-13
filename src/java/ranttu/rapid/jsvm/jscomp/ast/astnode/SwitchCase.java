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
import java.util.Optional;

/**
 * a switch case
 *
 * @author rapidhere@gmail.com
 * @version $id: SwitchCase.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class SwitchCase extends BaseAstNode {
    private Optional<Expression> test;
    private List<Statement> consequent = new ArrayList<>();

    public SwitchCase(JSONObject jsonObject) {
        super(jsonObject);
        test = Node.ofNullable(this, jsonObject, "test");
        jsonObject.getJSONArray("consequent").forEach((child) ->
            consequent.add(Node.of(this, (JSONObject) child)));
    }

    public Optional<Expression> getTest() {
        return test;
    }

    public List<Statement> getConsequent() {
        return consequent;
    }
}
