/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * binary expression, include logical expression
 *
 * @author rapidhere@gmail.com
 * @version $id: BinaryExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class BinaryExpression extends BaseAstNode implements Expression {
    private Expression left;
    private Expression right;
    private BinaryOperator operator;

    public BinaryExpression(JSONObject jsonObject) {
        super(jsonObject);
        operator = BinaryOperator.of(jsonObject.getString("operator"));
        left = Node.of(this, jsonObject, "left");
        right = Node.of(this, jsonObject, "right");
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public BinaryOperator getOperator() {
        return operator;
    }

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
}
