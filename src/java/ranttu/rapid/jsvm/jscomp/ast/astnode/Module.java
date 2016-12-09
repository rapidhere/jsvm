/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.ModuleItem;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.TopLevelNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Top Level Module
 *
 * @author rapidhere@gmail.com
 * @version $id: Module.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class Module extends BaseAstNode implements TopLevelNode {
    /** the body of the program */
    private List<ModuleItem> body = new ArrayList<>();

    public Module(JSONObject jsonObject) {
        super(jsonObject);

        jsonObject.getJSONArray("body").forEach((child) ->
            body.add(Node.of((JSONObject) child)));
    }

    public List<ModuleItem> getBody() {
        return body;
    }
}
