/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
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
        block = new BlockStatement(jsonObject.getJSONObject("block"));
        handler = new CatchClause(jsonObject.getJSONObject("handler"));
        finalizer = new BlockStatement(jsonObject.getJSONObject("finalizer"));
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
