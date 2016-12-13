/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Pattern;

/**
 * a catch clause in a `try...catch`
 *
 * @author rapidhere@gmail.com
 * @version $id: CatchClause.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class CatchClause extends BaseAstNode {
    private Pattern param;
    private BlockStatement body;

    public CatchClause(JSONObject jsonObject) {
        super(jsonObject);
        param = Node.of(this, jsonObject, "param");
        body = new BlockStatement(jsonObject.getJSONObject("body"));
    }

    public BlockStatement getBody() {
        return body;
    }

    public Pattern getParam() {
        return param;
    }
}
