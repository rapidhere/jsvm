/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Pattern;

import java.util.Optional;

/**
 * a variable declarator
 * @author rapidhere@gmail.com
 * @version $id: VariableDeclarator.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class VariableDeclarator extends BaseAstNode {
    private Pattern              id;
    private Optional<Expression> initExpression;

    public VariableDeclarator(JSONObject jsonObject) {
        super(jsonObject);

        id = Node.of(jsonObject, "id");
        initExpression = Node.ofNullable(jsonObject, "init");
    }

    public Pattern getId() {
        return id;
    }

    public Optional<Expression> getInitExpression() {
        return initExpression;
    }
}
