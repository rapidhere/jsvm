/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;

import java.util.Optional;

/**
 * a variable name
 *
 * @author rapidhere@gmail.com
 * @version $id: Name.java, v0.1 2016/12/13 dongwei.dq Exp $
 */
public class Name {
    private String id;
    private Optional<Expression> init;

    public Name(String id, Optional<Expression> init) {
        this.id = id;
        this.init = init;
    }

    public String getId() {
        return id;
    }

    public Optional<Expression> getInit() {
        return init;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
