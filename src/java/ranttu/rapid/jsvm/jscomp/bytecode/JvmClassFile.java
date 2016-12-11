/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.bytecode;

import java.io.OutputStream;

/**
 * a jvm class file generating helper
 * @author rapidhere@gmail.com
 * @version $id: JvmClassFile.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
public class JvmClassFile {
    private String className;
    private String superClassName;

    private OutputStream outputStream;

    public JvmClassFile(String className, String superClassName) {
        this.className = className;
        this.superClassName = superClassName;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
