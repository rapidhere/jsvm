/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import jdk.internal.org.objectweb.asm.ClassWriter;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;
import ranttu.rapid.jsvm.jscomp.comp.pass.CollectNamePass;
import ranttu.rapid.jsvm.jscomp.comp.pass.GenerateBytecodePass;
import ranttu.rapid.jsvm.jscomp.comp.pass.IrTransformPass;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
     * @param className the name of the compiled class
     */
    public Map<String, byte[]> compile(@Nonnull String className) throws IOException {
        return compile(className, "<dummy>");
    }

    /**
     * compile and output the bytes
     * @param className the name of the compiled class
     * @param sourceFileName the name of the source file
     */
    public Map<String, byte[]> compile(@Nonnull String className, @Nonnull String sourceFileName)
                                                                                                 throws IOException {
        // create new context
        context = new CompilingContext();

        context.className = $$.notBlank(className);
        context.sourceFileName = $$.notBlank(sourceFileName);
        context.ast = ast;

        // invoking passes
        invokePass(new IrTransformPass());
        invokePass(new CollectNamePass());
        invokePass(new GenerateBytecodePass());

        // write class
        Map<String, byte[]> ret = new HashMap<>();
        for (String clsName : context.moduleClasses.keySet()) {
            ClassNode clsNode = context.moduleClasses.get(clsName);

            ClassWriter cw = new ClassWriter(0);
            clsNode.$.accept(cw);
            ret.put(clsName, cw.toByteArray());
        }

        return ret;
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
