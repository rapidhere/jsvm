/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Top Level Program
 * @author rapidhere@gmail.com
 * @version $id: Program.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class Program extends BaseAstNode {
    /** the body of the program */
    private List<Statement> body = new ArrayList<>();

    private SourceType sourceType;

    public Program(JSONObject jsonObject) {
        super(jsonObject);

        jsonObject.getJSONArray("body").forEach((child) ->
            body.add(Node.of((JSONObject) child)));
        sourceType = SourceType.of(jsonObject.getString("sourceType"));
    }

    public List<Statement> getBody() {
        return body;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public enum SourceType {
        SCRIPT, MODULE
        ;

        public static SourceType of(String s) {
            return valueOf(s.toUpperCase());
        }
    }
}
