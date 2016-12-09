/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.TopLevelNode;

/**
 * The root of the ast tree
 *
 * @author rapidhere@gmail.com
 * @version $id: AbstractSyntaxTree.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class AbstractSyntaxTree {
    /** the root of the ast*/
    private TopLevelNode root;

    /**
     * get the root of ast
     */
    public TopLevelNode getRoot() {
        return root;
    }

    /**
     * build a ast from es-tree json
     * @param jsonObject a es-tree json
     */
    public static AbstractSyntaxTree fromJson(JSONObject jsonObject) {
        AbstractSyntaxTree tree = new AbstractSyntaxTree();
        tree.root = Node.of(jsonObject);

        return tree;
    }
}
