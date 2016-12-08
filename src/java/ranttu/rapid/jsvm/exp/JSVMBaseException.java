/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.exp;

/**
 * the base exception
 * @author rapidhere@gmail.com
 * @version $id: JSVMBaseException.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class JSVMBaseException extends RuntimeException {
    public JSVMBaseException(String message) {
        super(message);
    }

    public JSVMBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
