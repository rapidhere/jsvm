/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

import java.util.Optional;

import static ranttu.rapid.jsvm.common.ObjectUtil.cast;

/**
 * object property
 *
 * @author rapidhere@gmail.com
 * @version $id: Property.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class Property extends BaseAstNode {
    private Optional<Literal> literalKey = Optional.empty();
    private Optional<Identifier> identifierKey = Optional.empty();

    private Expression value;
    private PropertyType kind;

    public Property(JSONObject jsonObject) {
        super(jsonObject);

        Node key = Node.of(jsonObject, "key");
        if(key instanceof Literal) {
            literalKey = Optional.of(cast(key));
        } else {
            identifierKey = Optional.of(cast(key));
        }

        value = Node.of(jsonObject, "value");
        kind = PropertyType.of(jsonObject.getString("kind"));
    }

    public Optional<Identifier> getIdentifierKey() {
        return identifierKey;
    }

    public Optional<Literal> getLiteralKey() {
        return literalKey;
    }

    public Expression getValue() {
        return value;
    }

    public PropertyType getKind() {
        return kind;
    }

    public enum PropertyType {
        INIT, GET, SET
        ;

        public static PropertyType of(String v) {
            return PropertyType.valueOf(v.toUpperCase());
        }
    }
}
