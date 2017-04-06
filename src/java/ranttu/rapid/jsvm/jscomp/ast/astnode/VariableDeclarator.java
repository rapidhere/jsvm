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
 * a variable declarator
 * variable declarator only use as a property
 *
 * @author rapidhere@gmail.com
 * @version $id: VariableDeclarator.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class VariableDeclarator extends BaseAstNode {
    private Identifier           id;
    private Optional<Expression> initExpression;

    public VariableDeclarator(JSONObject jsonObject) {
        super(jsonObject);

        id = Node.of(this, jsonObject, "id");
        initExpression = Node.ofNullable(this, jsonObject, "init");
    }

    public Identifier getId() {
        return id;
    }

    public Optional<Expression> getInitExpression() {
        return initExpression;
    }
}
