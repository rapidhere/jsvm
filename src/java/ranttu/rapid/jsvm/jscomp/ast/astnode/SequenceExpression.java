/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * a sequence of expression
 *
 * @author rapidhere@gmail.com
 * @version $id: SequenceExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class SequenceExpression extends BaseAstNode implements Expression {
    private List<Expression> expressions = new ArrayList<>();

    public SequenceExpression(JSONObject jsonObject) {
        super(jsonObject);
        jsonObject.getJSONArray("expressions").forEach((child) ->
            expressions.add(Node.of((JSONObject) child)));
    }

    public List<Expression> getExpressions() {
        return expressions;
    }
}
