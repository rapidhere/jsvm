/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

import java.util.Optional;

/**
 * a break statement
 *
 * @author rapidhere@gmail.com
 * @version $id: BreakStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class BreakStatement extends BaseAstNode implements Statement {
    private Optional<Identifier> label;

    public BreakStatement(JSONObject jsonObject) {
        super(jsonObject);
        label = Node.ofNullable(this, jsonObject, "label");
    }

    public Optional<Identifier> getLabel() {
        return label;
    }
}
