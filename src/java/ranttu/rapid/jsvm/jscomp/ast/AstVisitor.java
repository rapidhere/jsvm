/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast;

import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * a ast visitor
 *
 * or `on` method return true for continue visit, return false for stop visit
 *
 * @author rapidhere@gmail.com
 * @version $id: AstVisitor.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
public interface AstVisitor {
    /**
     * default on-node visit
     * @param node the node
     * @return return should continue visit
     */
    default boolean visit(Node node) {
        return true;
    }

    // ~~~ specific visit

    default boolean on(Program program) {
        return visit(program);
    }

    default boolean on(VariableDeclaration variableDeclaration) {
        return visit(variableDeclaration);
    }
}
