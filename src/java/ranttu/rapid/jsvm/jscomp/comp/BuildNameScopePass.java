/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;

/**
 * the pass that build name scope
 *
 * @author rapidhere@gmail.com
 * @version $id: BuildNameScopePass.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
public class BuildNameScopePass extends CompilePass {
    private NamingEnvironment lexicalEnv;
    private NamingEnvironment variableEnv;

    public BuildNameScopePass(Compiler compiler) {
        super(compiler);

        // ~setup
        compiler.lexicalEnvironment = new NamingEnvironment();
        compiler.variableEnvironment = new NamingEnvironment();

        // rename
        this.lexicalEnv = compiler.lexicalEnvironment;
        this.variableEnv = compiler.variableEnvironment;
    }

    @Override
    public void beforePass() {
        // setup global environment
        lexicalEnv.enter();
        variableEnv.enter();
    }

    @Override
    public void afterPass() {
        // clear global environment
        lexicalEnv.leave();
        variableEnv.leave();
    }

    @Override
    public boolean on(Program program) {
        return true;
    }
}
