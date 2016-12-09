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

/**
 * @author rapidhere@gmail.com
 * @version $id: MemberExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class MemberExpression extends BaseAstNode implements Expression, Pattern {
    private Expression object;
    private Expression property;
    private boolean computed;

    public MemberExpression(JSONObject jsonObject) {
        super(jsonObject);
        object = Node.of(jsonObject, "object");
        property = Node.of(jsonObject, "property");
        computed = jsonObject.getBoolean("computed");
    }

    public Expression getObject() {
        return object;
    }

    public Expression getProperty() {
        return property;
    }

    public boolean isComputed() {
        return computed;
    }
}
