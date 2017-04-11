/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.jscomp.ast.astnode.FunctionDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
import ranttu.rapid.jsvm.jscomp.ast.enums.DeclarationType;
import ranttu.rapid.jsvm.jscomp.comp.CompilePass;
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
    public void on(Program program) {
        namingEnv.newScope(program);
    }

    @Override
    public void on(VariableDeclaration variableDeclaration) {
        // only let expression is supported now
        $$.should(variableDeclaration.getKind() == DeclarationType.LET);

        for(VariableDeclarator var: variableDeclaration.getDeclarations()) {
            namingEnv.addBinding(variableDeclaration, var.getId().getName(), var);
        }
    }

    @Override
    public void on(FunctionDeclaration functionDeclaration) {
        // add to last scope
        namingEnv.addBinding(functionDeclaration, functionDeclaration.getId().getName(), functionDeclaration);

        // add a new scope
        namingEnv.newScope(functionDeclaration);
    }
}
