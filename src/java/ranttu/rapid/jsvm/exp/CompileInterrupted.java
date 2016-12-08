/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.exp;

/**
 * the compile is interrupted
 *
 * @author rapidhere@gmail.com
 * @version $id: CompileInterrupted.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class CompileInterrupted extends CompileFailed {
    public CompileInterrupted() {
        super("compile is interrupted");
    }

    public CompileInterrupted(Throwable cause) {
        super("compile is interrupted", cause);
    }
}
