/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * a block of statement
 *
 * @author rapidhere@gmail.com
 * @version $id: BlockStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class BlockStatement extends BaseAstNode implements Statement {
    private List<Statement> body = new ArrayList<>();

    public BlockStatement() {

    }

    public BlockStatement(JSONObject jsonObject) {
        super(jsonObject);

        jsonObject.getJSONArray("body").forEach((child) ->
            body.add(Node.of(this, (JSONObject) child)));
    }

    public List<Statement> getBody() {
        return body;
    }
}
