/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import org.objectweb.asm.tree.ClassNode;

/**
 * the compiling context
 *
 * @author rapidhere@gmail.com
 * @version $id: CompilingContext.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public class CompilingContext {
    /** the file name of source*/
    public String    sourceFileName;

    /** compiled class */
    public ClassNode moduleClass;
}
