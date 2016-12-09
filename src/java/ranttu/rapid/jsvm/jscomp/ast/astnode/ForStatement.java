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

import static ranttu.rapid.jsvm.common.ObjectUtil.cast;

/**
 * a for statement
 *
 * @author rapidhere@gmail.com
 * @version $id: ForStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ForStatement extends BaseAstNode implements Statement {
    private Optional<VariableDeclaration> declarationInit = Optional.empty();
    private Optional<Expression>          expressionInit  = Optional.empty();

    private Optional<Expression>          test;
    private Optional<Expression>          update;

    public ForStatement(JSONObject jsonObject) {
        super(jsonObject);

        Optional<Node> init = Node.ofNullable(jsonObject, "init");
        if (init.isPresent()) {
            Node initNode = init.get();
            if (initNode instanceof VariableDeclaration) {
                declarationInit = Optional.of(cast(initNode));
            } else {
                expressionInit = Optional.of(cast(initNode));
            }
        }

        test = Node.ofNullable(jsonObject, "test");
        update = Node.ofNullable(jsonObject, "update");
    }


    public boolean hasInit() {
        return declarationInit.isPresent() || expressionInit.isPresent();
    }

    public Optional<VariableDeclaration> getDeclarationInit() {
        return declarationInit;
    }

    public Optional<Expression> getExpressionInit() {
        return expressionInit;
    }

    public Optional<Expression> getTest() {
        return test;
    }

    public Optional<Expression> getUpdate() {
        return update;
    }
}
