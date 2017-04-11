/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.enums;

/**
 * @author rapidhere@gmail.com
 * @version $id: VariableDeclaration.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public enum DeclarationType {
    VAR, LET, CONST

    ;

    public static DeclarationType of(String s) {
        return DeclarationType.valueOf(s.toUpperCase());
    }
}