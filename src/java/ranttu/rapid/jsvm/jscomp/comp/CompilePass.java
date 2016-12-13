/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * a compile pass
 *
 * @author rapidhere@gmail.com
 * @version $id: CompilePass.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
abstract public class CompilePass<T> {
    protected Compiler compiler;

    public CompilePass(Compiler compiler) {
        this.compiler = compiler;
    }

    /**
     * start the compile pass from here
     * @param astRoot the ast root
     */
    public void process(Program astRoot) {
        visit(astRoot);
    }

    // ~~~ visitors
    protected T visit(Node node) {
        if (node.isProgram()) {
            return visit((Program) node);
        } else if (node.isVariableDeclaration()) {
            return visit((VariableDeclaration) node);
        }

        // should not reach
        return null;
    }

    protected T visit(Program program) {
        return null;
    }

    protected T visit(VariableDeclaration variableDeclaration) {
        return null;
    }

    protected T visit(FunctionDeclaration functionDeclaration) {
        return null;
    }
}
