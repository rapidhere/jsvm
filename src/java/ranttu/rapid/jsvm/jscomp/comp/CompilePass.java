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
abstract public class CompilePass {
    protected CompilingContext context;

    /**
     * start the compile pass from here
     * @param astRoot the ast root
     */
    public void process(Program astRoot) {
        visit(astRoot);
        afterProcess();
    }

    /**
     * set the compiling context
     * @param compilingContext the context
     */
    final public void setContext(CompilingContext compilingContext) {
        this.context = compilingContext;
    }

    // ~~~ hooks

    /**
     * do something after processing on tree
     */
    protected void afterProcess() {

    }

    // ~~~ visitors
    private void visit(Node node) {
        if (node.is(Program.class)) {
            visit((Program) node);
        } else if (node.is(VariableDeclaration.class)) {
            visit((VariableDeclaration) node);
        }
    }

    private void visit(Program program) {
        on(program);
        program.getBody().forEach(this::visit);
    }

    private void visit(VariableDeclaration variableDeclaration) {
        on(variableDeclaration);
    }

    // ~~~ program node handlers
    protected void on(Program program) {
    }

    protected void on(VariableDeclaration variableDeclaration) {
    }

    protected void on(FunctionDeclaration functionDeclaration) {
    }
}
