/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.ast.AstVisitor;

/**
 * a compile pass
 *
 * @author rapidhere@gmail.com
 * @version $id: CompilePass.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
abstract public class CompilePass implements AstVisitor {
    protected Compiler compiler;

    public CompilePass(Compiler compiler) {
        this.compiler = compiler;
    }

    /**
     * invoke before pass invoking
     */
    public void beforePass() {

    }

    /**
     * invoke after pass invoking, weather success or failed
     */
    public void afterPass() {

    }
}
