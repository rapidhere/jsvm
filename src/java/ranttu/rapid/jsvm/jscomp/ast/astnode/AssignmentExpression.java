/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.exp.NotSupportedYet;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.enums.AssignmentOperator;

/**
 * a assignment expression
 *
 * @author rapidhere@gmail.com
 * @version $id: AssignmentExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class AssignmentExpression extends BaseAstNode implements Expression {
    private AssignmentOperator operator;
    private Expression left;
    private Expression right;

    public AssignmentExpression(JSONObject jsonObject) {
        super(jsonObject);
        operator = AssignmentOperator.of(jsonObject.getString("operator"));
        left = Node.of(this, jsonObject, "left");
        right = Node.of(this, jsonObject, "right");

        // ~~~ only assign is supported
        if(! $$.in(operator, AssignmentOperator.ASSIGN)) {
            throw new NotSupportedYet(this, "only support assign `=` now");
        }
    }

    public Expression getLeft() {
        return left;
    }

    public AssignmentOperator getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }
}
