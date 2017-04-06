/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;

/**
 * a function declaration
 *
 * @author rapidhere@gmail.com
 * @version $id: FunctionDeclaration.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class FunctionDeclaration extends Function {
    public FunctionDeclaration(JSONObject jsonObject) {
        super(jsonObject);
    }

    public Identifier getId() {
        return getIdOptional().get();
    }
}
