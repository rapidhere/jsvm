/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.asttype;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.astnode.BaseAstNode;
import ranttu.rapid.jsvm.jscomp.ast.astnode.BlockStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * abstract function
 *
 * @author rapidhere@gmail.com
 * @version $id: Function.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class Function extends BaseAstNode {
    private Optional<Identifier> id;
    private List<Pattern> params = new ArrayList<>();
    private BlockStatement body;
    private boolean generator;

    public Function(JSONObject jsonObject) {
        super(jsonObject);

        id = Node.ofNullable(jsonObject, "id");
        body = new BlockStatement(jsonObject.getJSONObject("body"));
        jsonObject.getJSONArray("params").forEach((child) ->
            params.add(Node.of((JSONObject) child)));
        generator = jsonObject.getBoolean("generator");
    }

    public Optional<Identifier> getIdOptional() {
        return id;
    }

    public List<Pattern> getParams() {
        return params;
    }

    public BlockStatement getBody() {
        return body;
    }

    public boolean isGenerator() {
        return generator;
    }
}
