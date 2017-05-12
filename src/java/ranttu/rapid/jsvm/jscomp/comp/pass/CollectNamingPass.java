/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp.pass;

import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.jscomp.ExportName;
import ranttu.rapid.jsvm.jscomp.ast.astnode.*;
import ranttu.rapid.jsvm.jscomp.comp.NamingEnvironment;
import ranttu.rapid.jsvm.runtime.JsModule;

import java.lang.reflect.Field;

/**
 * the pass that collect names
 *
 * @author rapidhere@gmail.com
 * @version $id: CollectNamingPass.java, v0.1 2017/4/19 dongwei.dq Exp $
 */
public class CollectNamingPass extends AstBasedCompilePass {
    private NamingEnvironment env;

    @Override
    protected void before() {
        context.namingEnv = env = new NamingEnvironment();
    }

    @Override
    protected void visit(Program program) {
        env.newScope(program);

        // add runtime
        for(Field field: JsModule.class.getDeclaredFields()) {
            if(field.isAnnotationPresent(ExportName.class)) {
                env.addVarBinding(program, field.getName());
            }
        }

        super.visit(program);
    }

    protected void visit(ImportDeclaration importDeclaration) {
        env.addVarBinding(importDeclaration,
            importDeclaration.getNamespace().getName());
    }

    @Override
    protected void visit(VariableDeclarator var) {
        env.addVarBinding(var, var.getId().getName());
        super.visit(var);
    }

    @Override
    protected void visit(FunctionDeclaration fun) {
        env.addVarBinding(fun, fun.getId().getName());
        env.newScope(fun);

        for(Identifier par: fun.getParams()) {
            env.addVarBinding(fun, par.getName());
        }

        super.visit(fun);
    }

    @Override
    protected void visit(FunctionExpression fun) {
        $$.should(! fun.getId().isPresent());
        env.newScope(fun);

        for(Identifier par: fun.getParams()) {
            env.addVarBinding(fun, par.getName());
        }
        super.visit(fun);
    }
}
