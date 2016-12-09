/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.asttype;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Module;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Script;

/**
 * a top level node
 *
 * @author rapidhere@gmail.com
 * @version $id: TopLevelNode.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public interface TopLevelNode extends Node {
    static TopLevelNode of(JSONObject jsonObject) {
        if(jsonObject.getString("sourceType").equals("script")) {
            return new Script(jsonObject);
        } else {
            return new Module(jsonObject);
        }
    }

    default boolean isModule() {
        return this instanceof Module;
    }
}
