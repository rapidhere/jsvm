/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.exp.ESTreeLoadFailed;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;

import static ranttu.rapid.jsvm.common.$$.cast;

/**
 * a literal
 *
 * @author rapidhere@gmail.com
 * @version $id: Literal.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class Literal<T> extends BaseAstNode implements Expression {
    private T value;

    private Literal(JSONObject jsonObject, T value) {
        super(jsonObject);
        this.value = value;
    }

    public T get() {
        return value;
    }

    public boolean isInt() {
        return value instanceof Integer;
    }

    public boolean isDouble() {return value instanceof Double; }

    public boolean isString() {
        return value instanceof String;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public int getInt() {
        return cast(value);
    }

    public String getString() {
        return cast(value);
    }

    public boolean getBoolean() {
        return cast(value);
    }

    public double getDouble() {
        return cast(value);
    }

    public String stringValue() {
        return String.valueOf(value);
    }

    public static Literal of(JSONObject jsonObject) {
        // TODO: support regexp
        Object v = jsonObject.get("value");

        if (v instanceof String) {
            return new Literal<>(jsonObject, cast(v));
        } else if (v instanceof Boolean) {
            return new Literal<>(jsonObject, cast(v));
        } else if (v instanceof Number) {
            return new Literal<>(jsonObject, cast(v));
        } else if (v == JSONObject.NULL) {
            // TODO: make sure can compare like this
            return new Literal<>(jsonObject, null);
        } else {
            throw new ESTreeLoadFailed("unsupported literal" + v);
        }
    }
}
