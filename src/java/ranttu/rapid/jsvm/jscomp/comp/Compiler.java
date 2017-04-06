/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import org.objectweb.asm.ClassWriter;
import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;

/**
 * the jvm compiler
 * @author rapidhere@gmail.com
 * @version $id: Compiler.java, v0.1 2016/12/10 dongwei.dq Exp $
 */
public class Compiler {
    protected AbstractSyntaxTree ast;

    protected CompilingContext   context;

    public Compiler(AbstractSyntaxTree ast) {
        this.ast = ast;
    }

    /**
     * compile and output the bytes
     * @param output output byte stream
     */
    public void compile(@Nonnull OutputStream output) throws IOException {
        // generate bytecode
        invokePass(new GenerateBytecodePass());

        // write class
        ClassWriter cw = new ClassWriter(0);
        context.moduleClass.accept(cw);
        output.write(cw.toByteArray());
    }

    /**
     * invoke a single pass
     * @param pass the pass
     */
    private void invokePass(CompilePass pass) {
        pass.setContext(context);
        pass.process(ast.getRoot());
    }
}
