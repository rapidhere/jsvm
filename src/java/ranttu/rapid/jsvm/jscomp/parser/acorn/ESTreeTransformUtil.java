/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.parser.acorn;

import org.json.JSONException;
import org.json.JSONObject;
import ranttu.rapid.jsvm.exp.CompileFailed;
import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;
import ranttu.rapid.jsvm.jscomp.ast.Location;
import ranttu.rapid.jsvm.jscomp.ast.astnode.BaseAstNode;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Identifier;
import ranttu.rapid.jsvm.jscomp.ast.astnode.Program;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclaration;
import ranttu.rapid.jsvm.jscomp.ast.astnode.VariableDeclarator;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Pattern;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

import javax.annotation.Nonnull;

/**
 * Transform es-tree to a ast
 * see <a href="https://github.com/estree/estree">es tree</a>
 *
 * @author rapidhere@gmail.com
 * @version $id: ESTreeTransformUtil.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
final public class ESTreeTransformUtil {
    // disable constructor
    private ESTreeTransformUtil() {
    }

    /**
     * transform a es tree to ast
     * @param esTreeJson the es tree json object
     * @return the ast
     */
    public static AbstractSyntaxTree transform(JSONObject esTreeJson) {
        AbstractSyntaxTree ast = new AbstractSyntaxTree();
        ast.setRoot(walkOnEsTree(esTreeJson));

        return ast;
    }

    private static BaseAstNode walkOnEsTree(JSONObject jsonNode) {
        try {
            String type = jsonNode.getString("type");

            switch (type) {
                case ES_TYPE_PROGRAM:
                    Program program = fromJsonNode(jsonNode, Program.class);
                    jsonNode.getJSONArray("body").forEach((jsonObject) ->
                        program.addStatement((Statement) walkOnEsTree((JSONObject) jsonObject)));
                    return program;

                case ES_TYPE_IDENTIFIER:
                    Identifier identifier = fromJsonNode(jsonNode, Identifier.class);
                    identifier.setName(jsonNode.getString("name"));
                    return identifier;

                case ES_TYPE_VAR_DEC:
                    VariableDeclaration declaration = fromJsonNode(jsonNode, VariableDeclaration.class);
                    declaration.setKind(VariableDeclaration.DeclarationType.of(jsonNode.getString("kind")));
                    jsonNode.getJSONArray("declarations").forEach((jsonObject) ->
                        declaration.addDeclaration((VariableDeclarator) walkOnEsTree((JSONObject) jsonObject)));
                    return declaration;

                case ES_TYPE_VAR_DECT:
                    VariableDeclarator declarator = fromJsonNode(jsonNode, VariableDeclarator.class);
                    declarator.setId((Pattern) walkOnEsTree(jsonNode.getJSONObject("id")));
                    return declarator;

                default:
                    throw new CompileFailed("acorn: unknown type: " + type);
            }
        } catch (JSONException e) {
            throw new CompileFailed("acorn: invalid es tree", e);
        }
    }

    @Nonnull
    private static <T extends BaseAstNode> T fromJsonNode(JSONObject jsonNode, Class<T> clazz) {

        T node;
        try {
            node = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // should not reach
            throw new RuntimeException(e);
        }

        // set node location
        node.setLocation(parseLocation(jsonNode.getJSONObject("loc").getJSONObject("start")),
            parseLocation(jsonNode.getJSONObject("loc").getJSONObject("end")));

        return node;
    }

    private static Location parseLocation(JSONObject locationObject) {
        Location loc = new Location();
        loc.line = locationObject.getInt("line");
        loc.column = locationObject.getInt("column");

        return loc;
    }

    private static final String
        ES_TYPE_PROGRAM = "Program",
        ES_TYPE_IDENTIFIER = "Identifier",
        ES_TYPE_VAR_DEC = "VariableDeclaration",
        ES_TYPE_VAR_DECT = "VariableDeclarator";
}
