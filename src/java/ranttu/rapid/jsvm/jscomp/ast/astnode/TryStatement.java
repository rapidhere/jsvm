/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

/**
 * a throw statement
 *
 * @author rapidhere@gmail.com
 * @version $id: TryStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class TryStatement extends BaseAstNode implements Statement {
    private BlockStatement block;
    private CatchClause handler;
    private BlockStatement finalizer;

    public TryStatement(JSONObject jsonObject) {
        super(jsonObject);
        block = Node.of(this, jsonObject.getJSONObject("block"));
        handler = Node.of(this, jsonObject.getJSONObject("handler"));

        // TODO: fix
        // finalizer = Node.of(this, jsonObject.getJSONObject("finalizer"));
    }

    public BlockStatement getBlock() {
        return block;
    }

    public CatchClause getHandler() {
        return handler;
    }

    public BlockStatement getFinalizer() {
        return finalizer;
    }
}
