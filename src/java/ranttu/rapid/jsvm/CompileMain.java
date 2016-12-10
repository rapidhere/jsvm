/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm;

import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;
import ranttu.rapid.jsvm.jscomp.comp.Compiler;
import ranttu.rapid.jsvm.jscomp.parser.AcornJSParser;
import ranttu.rapid.jsvm.jscomp.parser.Parser;

import java.io.ByteArrayOutputStream;

/**
 * the compile entry
 *
 * @author rapidhere@gmail.com
 * @version $id: CompileMain.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class CompileMain {
    public static void main(String args[]) {
        Parser parser = new AcornJSParser();
        AbstractSyntaxTree ast = parser.parse("var a = 1;");

        Compiler compiler = new Compiler(ast);
        compiler.compile(new ByteArrayOutputStream(1024));
    }
}
