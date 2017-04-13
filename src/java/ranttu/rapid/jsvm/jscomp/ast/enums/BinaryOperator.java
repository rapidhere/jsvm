/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.enums;

/**
 * @author rapidhere@gmail.com
 * @version $id: BinaryOperator.java, v0.1 2017/4/13 dongwei.dq Exp $
 */

public enum BinaryOperator {
    EQUAL("=="),
    NOT_EQUAL("!="),
    STRONG_EQUAL("==="),
    NOT_STRONG_EQUAL("!=="),
    LESS("<"),
    LESS_EQUAL("<="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    LEFT_SHIFT("<<"),
    RIGHT_SHIFT(">>"),
    RIGHT_SHIFT_NO_SIGN(">>>"),
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),
    OR("|"),
    XOR("^"),
    AND("&"),
    IN("in"),
    INSTANCE_OF("instanceof"),

    LOGICAL_OR("||"),
    LOGICAL_AND("&&"),
    ;

    private String token;

    BinaryOperator(String token) {
        this.token = token;
    }

    public static BinaryOperator of(String token) {
        for(BinaryOperator operator: values()) {
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