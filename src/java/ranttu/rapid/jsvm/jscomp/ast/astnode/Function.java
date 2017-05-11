/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ranttu.rapid.jsvm.common.$$.cast;

/**
 * abstract function
 *
 * @author rapidhere@gmail.com
 * @version $id: Function.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
abstract public class Function extends BaseAstNode {
    private Optional<Identifier> id;
    private List<Identifier> params = new ArrayList<>();
    private BlockStatement body;
    private boolean generator;
    private boolean async;

    public Function(JSONObject jsonObject) {
        super(jsonObject);

        id = Node.ofNullable(this, jsonObject, "id");
        Node _body = Node.of(this, jsonObject, "body");
        if(_body instanceof BlockStatement) {
            body = cast(_body);
        } else {
            body = new BlockStatement();
            body.getBody().add(new ExpressionStatement((Expression) _body));
        }

        jsonObject.getJSONArray("params").forEach((child) ->
            params.add(Node.of(this, (JSONObject) child)));
        generator = jsonObject.getBoolean("generator");
        async = jsonObject.getBoolean("async");

        // generator function is not supported yet
        $$.should(! isGenerator());
    }

    public Optional<Identifier> getIdOptional() {
        return id;
    }

    public List<Identifier> getParams() {
        return params;
    }

    public BlockStatement getBody() {
        return body;
    }

    public boolean isGenerator() {
        return generator;
    }

    public boolean isAsync() { return async;}
}
