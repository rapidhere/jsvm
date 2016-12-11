/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;
import ranttu.rapid.jsvm.jscomp.bytecode.JvmClassFile;

import javax.annotation.Nonnull;
import java.io.OutputStream;

/**
 * the jvm compiler
 * @author rapidhere@gmail.com
 * @version $id: Compiler.java, v0.1 2016/12/10 dongwei.dq Exp $
 */
public class Compiler {
    protected AbstractSyntaxTree ast;

    protected JvmClassFile classFile;

    // ~~~ the naming scope
    protected NamingEnvironment lexicalEnvironment;
    protected NamingEnvironment variableEnvironment;

    public Compiler(AbstractSyntaxTree ast) {
        this.ast = ast;
    }

    /**
     * compile and output the bytes
     * @param output output byte stream
     */
    public void compile(@Nonnull OutputStream output) {
        // TODO
        classFile = new JvmClassFile("", "");
        classFile.setOutputStream(output);

        // ~~~ build name scope
        invokePass(new BuildNameScopePass(this));

    }

    /**
     * invoke a single pass
     * @param pass the pass
     */
    private void invokePass(CompilePass pass) {
        try {
            pass.beforePass();
            ast.getRoot().visit(pass);
        } finally {
            pass.afterPass();
        }
    }
}
