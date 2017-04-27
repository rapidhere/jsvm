/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.exp.NotSupportedYet;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;

import java.util.Optional;

/**
 * @author rapidhere@gmail.com
 * @version $id: FunctionExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class FunctionExpression extends Function implements Expression {
    public FunctionExpression(JSONObject jsonObject) {
        super(jsonObject);

        if (getId().isPresent()) {
            throw new NotSupportedYet(this, "named function expression is not supported yet");
        }
    }

    public Optional<Identifier> getId() {
        return super.getIdOptional();
    }
}
