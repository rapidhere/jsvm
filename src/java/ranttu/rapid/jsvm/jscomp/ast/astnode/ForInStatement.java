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
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

import java.util.Optional;

import static ranttu.rapid.jsvm.common.$.cast;

/**
 * a for in statement
 *
 * @author rapidhere@gmail.com
 * @version $id: ForInStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ForInStatement extends BaseAstNode implements Statement {
    private Optional<VariableDeclaration> declarationLeft = Optional.empty();
    private Optional<Pattern>             patternLeft     = Optional.empty();
    private Expression                    right;
    private Statement                     body;

    public ForInStatement(JSONObject jsonObject) {
        super(jsonObject);

        Node left = Node.of(this, jsonObject, "left");
        if (left instanceof VariableDeclaration) {
            declarationLeft = Optional.of(cast(left));
        } else {
            patternLeft = Optional.of(cast(left));
        }

        right = Node.of(this, jsonObject, "right");
        body = Node.of(this, jsonObject, "body");
    }

    public Optional<VariableDeclaration> getDeclarationLeft() {
        return declarationLeft;
    }

    public Optional<Pattern> getPatternLeft() {
        return patternLeft;
    }

    public Expression getRight() {
        return right;
    }

    public Statement getBody() {
        return body;
    }
}
