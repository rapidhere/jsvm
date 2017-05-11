/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import ranttu.rapid.jsvm.exp.NotSupportedYet;
import ranttu.rapid.jsvm.jscomp.ast.astnode.*;
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
        } else if (node.is(NewExpression.class)) {
            visit(node.as(NewExpression.class));
        } else if (node.is(CallExpression.class)) {
            visit((CallExpression) node);
        } else if (node.is(ThisExpression.class)) {
            visit(node.as(ThisExpression.class));
        } else if (node.is(FunctionExpression.class)) {
            visit(node.as(FunctionExpression.class));
        } else if (node.is(BinaryExpression.class)) {
            visit(node.as(BinaryExpression.class));
        } else if (node.is(IfStatement.class)) {
            visit(node.as(IfStatement.class));
        } else if (node.is(WhileStatement.class)) {
            visit(node.as(WhileStatement.class));
        } else if (node.is(ExportNamedDeclaration.class)) {
            visit(node.as(ExportNamedDeclaration.class));
        } else if (node.is(AwaitExpression.class)) {
            visit(node.as(AwaitExpression.class));
        } else {
            throw new NotSupportedYet(node);
        }
    }

    protected void visit(AwaitExpression awaitExpression) {
        visit(awaitExpression.getArgument());
    }

    protected void visit(ExportNamedDeclaration exportNamedDeclaration) {
        visit(exportNamedDeclaration.getDeclaration());
    }

    protected void visit(WhileStatement whileStatement) {
        visit(whileStatement.getTest());
        visit(whileStatement.getBody());
    }

    protected void visit(IfStatement ifs) {
        visit(ifs.getTest());
        visit(ifs.getConsequent());
        if (ifs.getAlternate().isPresent()) {
            visit(ifs.getAlternate().get());
        }
    }

    protected void visit(NewExpression newExp) {
        visit(newExp.getCallee());
        newExp.getArguments().forEach(this::visit);
    }

    protected void visit(BinaryExpression binaryExpression) {
        visit(binaryExpression.getLeft());
        visit(binaryExpression.getRight());
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
