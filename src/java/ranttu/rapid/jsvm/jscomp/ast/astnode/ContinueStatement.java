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
 * a continue statement
 *
 * @author rapidhere@gmail.com
 * @version $id: ContinueStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ContinueStatement extends BaseAstNode implements Statement {
    private Optional<Identifier> label;

    public ContinueStatement(JSONObject jsonObject) {
        super(jsonObject);
        label = Node.ofNullable(jsonObject, "label");
    }

    public Optional<Identifier> getLabel() {
        return label;
    }
}
