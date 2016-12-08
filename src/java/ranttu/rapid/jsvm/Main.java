/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm;

import ranttu.rapid.jsvm.jscomp.parser.JSCompiler;
import ranttu.rapid.jsvm.jscomp.parser.acorn.AcornJSCompiler;

/**
 * @author rapidhere@gmail.com
 * @version $id: Main.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class Main {
    public static void main(String args[]) {
        JSCompiler compiler = new AcornJSCompiler();
        compiler.parse("var a = 1;");
    }
}
