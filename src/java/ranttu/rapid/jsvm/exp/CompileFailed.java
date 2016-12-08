/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.exp;

/**
 * common compile failed error
 * @author rapidhere@gmail.com
 * @version $id: CompileFailed.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class CompileFailed extends JSVMBaseException{
    public CompileFailed(String message) {
        super(message);
    }

    public CompileFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
