/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Function;

import java.util.Optional;

/**
 * @author rapidhere@gmail.com
 * @version $id: FunctionExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class FunctionExpression extends Function {
    public FunctionExpression(JSONObject jsonObject) {
        super(jsonObject);
    }

    public Optional<Identifier> getId() {
        return super.getIdOptional();
    }
}
