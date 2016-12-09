/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * a conditional expression
 *
 * @author rapidhere@gmail.com
 * @version $id: ConditionalExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ConditionalExpression extends BaseAstNode implements Expression {
    private Expression test;
    private Expression alternate;
    private Expression consequent;

    public ConditionalExpression(JSONObject jsonObject) {
        super(jsonObject);
        test = Node.of(jsonObject, "test");
        alternate = Node.of(jsonObject, "alternate");
        consequent = Node.of(jsonObject, "consequent");
    }

    public Expression getConsequent() {
        return consequent;
    }

    public Expression getTest() {
        return test;
    }

    public Expression getAlternate() {
        return alternate;
    }
}
