/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.exp;

import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * common compile failed error
 * @author rapidhere@gmail.com
 * @version $id: CompileError.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class CompileError extends JSVMBaseException {
    public CompileError(String message) {
        super(message);
    }

    public CompileError(String message, Throwable cause) {
        super(message, cause);
    }

    public CompileError(Node node, String message) {
        super(formatErrorMessage(node, message));
    }

    public CompileError(Node node, String message, Throwable cause) {
        super(formatErrorMessage(node, message), cause);
    }

    private static String formatErrorMessage(Node node, String message) {
        return String.format("(%d, %d): %s: %s", node.getStartLocation().getLine(), node
            .getStartLocation().getColumn(), node.getClass().getSimpleName(), message);
    }
}
