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

import java.util.Optional;

/**
 * a function return statement
 *
 * @author rapidhere@gmail.com
 * @version $id: ReturnStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ReturnStatement extends BaseAstNode implements Statement {
    private Optional<Expression> argument;

    public ReturnStatement(JSONObject jsonObject) {
        super(jsonObject);
        argument = Node.ofNullable(jsonObject, "argument");
    }

    public Optional<Expression> getArgument() {
        return argument;
    }
}
