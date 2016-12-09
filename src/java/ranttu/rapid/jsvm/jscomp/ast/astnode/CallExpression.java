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
 * a function call expression
 *
 * @author rapidhere@gmail.com
 * @version $id: CallExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class CallExpression extends BaseAstNode implements Expression {
    private Expression callee;
    private List<Expression> arguments = new ArrayList<>();

    public CallExpression(JSONObject jsonObject) {
        super(jsonObject);
        callee = Node.of(jsonObject, "callee");
        jsonObject.getJSONArray("arguments").forEach((child) ->
            arguments.add(Node.of((JSONObject) child)));
    }

    public Expression getCallee() {
        return callee;
    }

    public List<Expression> getArguments() {
        return arguments;
    }
}
