/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.enums.BinaryOperator;

/**
 * binary expression, include logical expression
 *
 * @author rapidhere@gmail.com
 * @version $id: BinaryExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class BinaryExpression extends BaseAstNode implements Expression {
    private Expression left;
    private Expression right;
    private BinaryOperator operator;

    public BinaryExpression(JSONObject jsonObject) {
        super(jsonObject);
        operator = BinaryOperator.of(jsonObject.getString("operator"));
        left = Node.of(this, jsonObject, "left");
        right = Node.of(this, jsonObject, "right");
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public BinaryOperator getOperator() {
        return operator;
    }
}
