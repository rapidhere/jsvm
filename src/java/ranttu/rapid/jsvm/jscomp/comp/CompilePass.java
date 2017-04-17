/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.ast.astnode.AssignmentExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.BinaryExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.BlockStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ExpressionStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Literal;
import ranttu.rapid.jsvm.jscomp.ast.astnode.MemberExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ObjectExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ReturnStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
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
     *
     * @param astRoot the ast root
     */
    public void process(Program astRoot) {
        beforeProcess();
        visit(astRoot);
        afterProcess();
    }

    /**
     * set the compiling context
     *
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

    /**
     * do something before processing on tree
     */
    protected void beforeProcess() {
    }

    // ~~~ visitors
    protected void visit(Node node) {
        if (node.is(Program.class)) {
            visit((Program) node);
        } else if (node.is(VariableDeclaration.class)) {
            visit((VariableDeclaration) node);
        } else if (node.is(FunctionDeclaration.class)) {
            visit((FunctionDeclaration) node);
        } else if (node.is(VariableDeclarator.class)) {
            visit((VariableDeclarator) node);
        } else if (node.is(FunctionExpression.class)) {
            visit((FunctionExpression) node);
        } else if (node.is(Literal.class)) {
            visit((Literal) node);
        } else if (node.is(BinaryExpression.class)) {
            visit((BinaryExpression) node);
        } else if (node.is(ObjectExpression.class)) {
            visit((ObjectExpression) node);
        } else if (node.is(ExpressionStatement.class)) {
            visit((ExpressionStatement) node);
        } else if (node.is(AssignmentExpression.class)) {
            visit((AssignmentExpression) node);
        } else if (node.is(Identifier.class)) {
            visit((Identifier) node);
        } else if (node.is(MemberExpression.class)) {
            visit((MemberExpression) node);
        } else if (node.is(ReturnStatement.class)) {
            visit((ReturnStatement) node);
        } else if (node.is(BlockStatement.class)) {
            visit((BlockStatement) node);
        }
    }

    protected void visit(Program program) {
        program.getBody().forEach(this::visit);
    }

    protected void visit(VariableDeclaration variableDeclaration) {
        variableDeclaration.getDeclarations().forEach(this::visit);
    }

    protected void visit(VariableDeclarator variableDeclarator) {
        if (variableDeclarator.getInitExpression().isPresent()) {
            visit(variableDeclarator.getInitExpression().get());
        }
    }

    protected void visit(FunctionExpression functionExpression) {
        visit(functionExpression.getBody());
    }

    protected void visit(FunctionDeclaration functionDeclaration) {
        visit(functionDeclaration.getBody());
    }

    protected void visit(Literal literal) {
    }

    protected void visit(Identifier identifier) {
    }

    protected void visit(ObjectExpression objectExpression) {
        objectExpression.getProperties().forEach(this::visit);
    }

    protected void visit(BinaryExpression binaryExpression) {
        visit(binaryExpression.getLeft());
        visit(binaryExpression.getRight());
    }

    protected void visit(ExpressionStatement statement) {
        visit(statement.getExpression());
    }

    protected void visit(AssignmentExpression expression) {
        visit(expression.getLeft());
        visit(expression.getRight());
    }

    protected void visit(MemberExpression memExp) {
    }

    protected void visit(BlockStatement blockStatement) {
        blockStatement.getBody().forEach(this::visit);
    }

    protected void visit(ReturnStatement returnStatement) {
        if (returnStatement.getArgument().isPresent()) {
            visit(returnStatement.getArgument().get());
        }
    }
}
