/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Function;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionExpression;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
import ranttu.rapid.jsvm.jscomp.ast.enums.DeclarationType;
import ranttu.rapid.jsvm.jscomp.comp.CompilePass;
import ranttu.rapid.jsvm.jscomp.comp.Name;
import ranttu.rapid.jsvm.jscomp.comp.NamingEnvironment;

/**
 * the pass that collecting names
 *
 * @author rapidhere@gmail.com
 * @version $id: CollectNamePass.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public class CollectNamePass extends CompilePass {
    /** the naming env */
    private NamingEnvironment namingEnv;

    @Override
    public void beforeProcess() {
        context.namingEnv = namingEnv = new NamingEnvironment();
    }

    @Override
    public void visit(Program program) {
        namingEnv.newScope(program);
        super.visit(program);
    }

    @Override
    public void visit(VariableDeclaration variableDeclaration) {
        $$.shouldIn(variableDeclaration.getKind(), DeclarationType.LET, DeclarationType.VAR);

        for (VariableDeclarator var : variableDeclaration.getDeclarations()) {
            if (variableDeclaration.getKind() == DeclarationType.LET) {
                namingEnv.addBinding(variableDeclaration, var.getId().getName(), var);
            } else {
                namingEnv.addVarBinding(variableDeclaration, var.getId().getName(), var);
            }
        }
    }

    @Override
    public void visit(FunctionDeclaration functionDeclaration) {
        // add to var scope
        namingEnv.addVarBinding(functionDeclaration, functionDeclaration.getId().getName(),
            functionDeclaration);

        addFunctionNamings(functionDeclaration);
        super.visit(functionDeclaration);
    }

    @Override
    public void visit(FunctionExpression functionExpression) {
        addFunctionNamings(functionExpression);
        super.visit(functionExpression);
    }

    private void addFunctionNamings(Function function) {
        namingEnv.newScope(function);

        for (Identifier par : function.getParams()) {
            namingEnv.addBinding(function, new Name(par.getName()));
        }
    }
}
