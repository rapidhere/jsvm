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
 * @author rapidhere@gmail.com
 * @version $id: IfStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class IfStatement extends BaseAstNode implements Statement {
    private Expression test;
    private Statement consequent;
    private Optional<Statement> alternate;

    public IfStatement(JSONObject jsonObject) {
        super(jsonObject);
        test = Node.of(this, jsonObject, "test");
        consequent = Node.of(this, jsonObject, "consequent");
        alternate = Node.ofNullable(this, jsonObject, "alternate");
    }

    public Expression getTest() {
        return test;
    }

    public Statement getConsequent() {
        return consequent;
    }

    public Optional<Statement> getAlternate() {
        return alternate;
    }
}
