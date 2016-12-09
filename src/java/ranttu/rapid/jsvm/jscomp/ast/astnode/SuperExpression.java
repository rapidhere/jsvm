/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;

/**
 * same as this, we consider super as a expression
 *
 * @author rapidhere@gmail.com
 * @version $id: SuperExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class SuperExpression extends BaseAstNode implements Expression {
    public SuperExpression(JSONObject jsonObject) {
        super(jsonObject);
    }
}
