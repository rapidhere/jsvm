/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.jscomp.ast.asttype.Declaration;
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
    private Declaration declaration;

    public Name(String id, Declaration declaration) {
        this.id = id;
        this.declaration = declaration;
    }

    public String getId() {
        return id;
    }

    public Declaration getDeclaration() {
        return  declaration;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
