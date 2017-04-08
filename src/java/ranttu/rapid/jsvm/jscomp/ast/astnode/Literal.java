/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.exp.ESTreeLoadFailed;

import static ranttu.rapid.jsvm.common.$$.cast;

/**
 * a literal
 *
 * @author rapidhere@gmail.com
 * @version $id: Literal.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class Literal<T> extends BaseAstNode {
    private T value;

    private Literal(JSONObject jsonObject, T value) {
        super(jsonObject);
        this.value = value;
    }

    public T get() {
        return value;
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
