/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * a object expression
 *
 * @author rapidhere@gmail.com
 * @version $id: ObjectExpression.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ObjectExpression extends BaseAstNode implements Expression {
    private List<Property> properties = new ArrayList<>();

    public ObjectExpression(JSONObject jsonObject) {
        super(jsonObject);
        jsonObject.getJSONArray("properties").forEach((child) ->
            properties.add(new Property((JSONObject) child)));
    }

    public List<Property> getProperties() {
        return properties;
    }
}
