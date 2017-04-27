/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import ranttu.rapid.jsvm.exp.NotSupportedYet;
import ranttu.rapid.jsvm.jscomp.ast.astnode.AssignmentExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.BlockStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.CallExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ExpressionStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Literal;
import ranttu.rapid.jsvm.jscomp.ast.astnode.MemberExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ObjectExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ReturnStatement;
import ranttu.rapid.jsvm.jscomp.ast.astnode.ThisExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * a compile pass that based on ast-tree
 *
 * @author rapidhere@gmail.com
 * @version $id: AstBasedCompilePass.java, v0.1 2017/4/19 dongwei.dq Exp $
 */
abstract public class AstBasedCompilePass extends CompilePass {
    @Override
    protected void start() {
        visit(context.ast.getRoot());
    }

    // visitors
    protected void visit(Node node) {
        if (node.is(Program.class)) {
            visit((Program) node);
        } else if (node.is(VariableDeclaration.class)) {
            visit((VariableDeclaration) node);
        } else if (node.is(FunctionDeclaration.class)) {
            visit((FunctionDeclaration) node);
        } else if (node.is(VariableDeclarator.class)) {
            visit((VariableDeclarator) node);
        } else if (node.is(Literal.class)) {
            visit((Literal) node);
        } else if (node.is(ObjectExpression.class)) {
            visit((ObjectExpression) node);
        } else if (node.is(ExpressionStatement.class)) {
            visit((ExpressionStatement) node);
        } else if (node.is(AssignmentExpression.class)) {
            visit((AssignmentExpression) node);
        } else if (node.is(MemberExpression.class)) {
            visit((MemberExpression) node);
        } else if (node.is(ReturnStatement.class)) {
            visit((ReturnStatement) node);
        } else if (node.is(BlockStatement.class)) {
            visit((BlockStatement) node);
        } else if (node.is(Identifier.class)) {
            visit((Identifier) node);
        } else if (node.is(CallExpression.class)) {
            visit((CallExpression) node);
        } else if (node.is(ThisExpression.class)) {
            visit(node.as(ThisExpression.class));
        } else if (node.is(FunctionExpression.class)) {
            visit(node.as(FunctionExpression.class));
        } else {
            throw new NotSupportedYet(node);
        }
    }

    protected void visit(FunctionExpression functionExpression) {
        visit(functionExpression.getBody());
    }

    protected void visit(ThisExpression thisExp) {

    }

    protected void visit(CallExpression call) {
        visit(call.getCallee());
        call.getArguments().forEach(this::visit);
    }

    protected void visit(VariableDeclaration variableDeclaration) {
        variableDeclaration.getDeclarations().forEach(this::visit);
    }

    protected void visit(VariableDeclarator variableDeclarator) {
        if (variableDeclarator.getInitExpression().isPresent()) {
            visit(variableDeclarator.getInitExpression().get());
        }
    }

    protected void visit(FunctionDeclaration function) {
        visit(function.getBody());
    }

    protected void visit(Literal literal) {
    }

    protected void visit(ObjectExpression objExp) {
        objExp.getProperties().forEach((property) -> {
            visit(property.getKey());
            visit(property.getValue());
        });
    }

    protected void visit(ExpressionStatement statement) {
        visit(statement.getExpression());
    }

    protected void visit(Identifier identifier) {
    }

    protected void visit(AssignmentExpression assignExp) {
        visit(assignExp.getLeft());
        visit(assignExp.getRight());
    }

    protected void visit(MemberExpression memExp) {
        visit(memExp.getObject());
        visit(memExp.getProperty());
    }

    protected void visit(BlockStatement blockStatement) {
        blockStatement.getBody().forEach(this::visit);
    }

    protected void visit(ReturnStatement returnStatement) {
        if (returnStatement.getArgument().isPresent()) {
            visit(returnStatement.getArgument().get());
        }
    }

    protected void visit(Program program) {
        program.getBody().forEach(this::visit);
    }
}
