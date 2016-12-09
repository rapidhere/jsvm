/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

/**
 * a alone semicolon
 *
 * @author rapidhere@gmail.com
 * @version $id: EmptyStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class EmptyStatement extends BaseAstNode implements Statement {
    public EmptyStatement(JSONObject jsonObject) {
        super(jsonObject);
    }
}
