/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Function;

/**
 * @author rapidhere@gmail.com
 * @version $id: ArrowFunctionExpression.java, v0.1 2016/12/10 dongwei.dq Exp $
 */
public class ArrowFunctionExpression extends Function implements Expression {
    private boolean expression;

    public ArrowFunctionExpression(JSONObject jsonObject) {
        super(jsonObject);
        expression = jsonObject.getBoolean("expression");
    }

    public boolean isExpression() {
        return expression;
    }
}
