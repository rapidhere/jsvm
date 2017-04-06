/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;

/**
 * the pass that generate bytecode
 *
 * @author rapidhere@gmail.com
 * @version $id: GenerateBytecodePass.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public class GenerateBytecodePass extends CompilePass {
    @Override
    protected void on(Program program) {
        // whole js program(file) as a top java class
        context.newClass()
            .acc_public()
            .source(context.sourceFileName)
            .name(getClassName())
            .inherit(Object.class);
    }

    @Override
    protected void on(VariableDeclaration variableDeclaration) {

    }

    /**
     * get the top class name of the source file
     * @return the class name
     */
    private String getClassName() {
        // TODO
        return context.sourceFileName;
    }
}
