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
 * a array expression
 *
 * @author rapidhere@gmail.com
 * @version $id: ArrayExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ArrayExpression extends BaseAstNode implements Expression {
    private List<Expression> elements = new ArrayList<>();

    public ArrayExpression(JSONObject jsonObject) {
        super(jsonObject);
        jsonObject.getJSONArray("elements").forEach((child) ->
            elements.add(Node.of(this, (JSONObject) child)));
    }

    public List<Expression> getElements() {
        return elements;
    }
}
