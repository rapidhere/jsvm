/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Pattern;

import java.util.Optional;

import static ranttu.rapid.jsvm.common.ObjectUtil.cast;

/**
 * a assignment expression
 *
 * @author rapidhere@gmail.com
 * @version $id: AssignmentExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class AssignmentExpression extends BaseAstNode implements Expression {
    private AssignmentOperator operator;

    private Optional<Pattern> patternLeft = Optional.empty();
    private Optional<Expression> expressionLeft = Optional.empty();

    private Expression right;

    public AssignmentExpression(JSONObject jsonObject) {
        super(jsonObject);
        operator = AssignmentOperator.of(jsonObject.getString("operator"));

        Node left = Node.of(jsonObject, "left");
        if(left instanceof Pattern) {
            patternLeft = Optional.of(cast(left));
        } else {
            expressionLeft = Optional.of(cast(left));
        }

        right = Node.of(jsonObject, "right");
    }

    public Optional<Expression> getExpressionLeft() {
        return expressionLeft;
    }

    public Optional<Pattern> getPatternLeft() {
        return patternLeft;
    }

    public AssignmentOperator getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

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
}
