/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.enums;

/**
 * @author rapidhere@gmail.com
 * @version $id: AssignmentOperator.java, v0.1 2017/4/14 dongwei.dq Exp $
 */

public enum AssignmentOperator {
    ASSIGN("="),
    ADD_ASSIGN("+="),
    SUB_ASSIGN("-="),
    MUL_ASSIGN("*="),
    DIV_ASSIGN("/="),
    MOD_ASSIGN("%="),
    LSH_ASSIGN("<<="),
    RSH_ASSIGN(">>="),
    RSN_ASSIGN(">>>="),
    OR_ASSIGN("|="),
    NOT_ASSIGN("^="),
    AND_ASSIGN("&="),

    ;

    private String token;

    AssignmentOperator(String token) {
        this.token = token;
    }

    public static AssignmentOperator of(String token) {
        for(AssignmentOperator operator: values()) {
            if(operator.token.equals(token)) {
                return operator;
            }
        }
        // should not reach
        return null;
    }

    public String getToken() {
        return token;
    }
}