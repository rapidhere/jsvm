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

/**
 * a statement with only a expression
 *
 * @author rapidhere@gmail.com
 * @version $id: ExpressionStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ExpressionStatement extends BaseAstNode implements Statement {
    private Expression expression;

    public ExpressionStatement(JSONObject jsonObject) {
        super(jsonObject);
        expression = Node.of(this, jsonObject, "expression");
    }

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
