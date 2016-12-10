/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

import java.util.Optional;

/**
 * a yield expression
 *
 * @author rapidhere@gmail.com
 * @version $id: YieldExpression.java, v0.1 2016/12/10 dongwei.dq Exp $
 */
public class YieldExpression extends BaseAstNode implements Expression {
    private Optional<Expression> argument;
    private boolean delegate;

    public YieldExpression(JSONObject jsonObject) {
        super(jsonObject);
        argument = Node.ofNullable(jsonObject, "argument");
        delegate = jsonObject.getBoolean("delegate");
    }

    public boolean isDelegate() {
        return delegate;
    }

    public Optional<Expression> getArgument() {
        return argument;
    }
}
