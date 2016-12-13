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
 * expression with a unary operator
 * NOTE: include update expression in es tree
 *
 * @author rapidhere@gmail.com
 * @version $id: UnaryExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class UnaryExpression extends BaseAstNode implements Expression {
    private UnaryOperator operator;
    private boolean prefix;
    private Expression argument;

    public UnaryExpression(JSONObject jsonObject) {
        super(jsonObject);
        operator = UnaryOperator.of(jsonObject.getString("operator"));
        prefix = jsonObject.getBoolean("prefix");
        argument = Node.of(this, jsonObject, "argument");
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public boolean isPrefix() {
        return prefix;
    }

    public Expression getArgument() {
        return argument;
    }

    public enum UnaryOperator {
        MINUS("-"),
        PLUS("+"),
        NOT("!"),
        NEG("~"),
        TYPEOF("typeof"),
        VOID("void"),
        DELETE("delete"),

        INC("++"),
        DEC("--"),

        ;

        private String token;

        UnaryOperator(String token) {
            this.token = token;
        }

        public static UnaryOperator of(String token) {
            for(UnaryOperator operator: values()) {
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
