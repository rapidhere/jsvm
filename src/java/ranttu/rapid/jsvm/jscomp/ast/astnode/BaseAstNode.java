/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONException;
import org.json.JSONObject;
import ranttu.rapid.jsvm.exp.CompileFailed;
import ranttu.rapid.jsvm.jscomp.ast.Location;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Pattern;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

/**
 * abstract ast node
 *
 * @author rapidhere@gmail.com
 * @version $id: BaseAstNode.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
abstract public class BaseAstNode implements Node {
    /** start location */
    private Location startLocation;

    /** end location */
    private Location endLocation;

    @Override
    public Location getStartLocation() {
        return startLocation;
    }

    @Override
    public Location getEndLocation() {
        return endLocation;
    }

    public void setLocation(Location startLocation, Location endLocation) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public static BaseAstNode fromJson(JSONObject jsonObject) {
        try {
            BaseAstNode node = routeNodeByType(jsonObject);

            // set location
            node.setLocation(parseLocation(jsonObject.getJSONObject("loc").getJSONObject("start")),
                    parseLocation(jsonObject.getJSONObject("loc").getJSONObject("end")));

            return node;
        } catch (JSONException e) {
            throw new CompileFailed("acorn: invalid es tree", e);
        }
    }

    private static BaseAstNode routeNodeByType(JSONObject jsonObject) {
        String type = jsonObject.getString("type");

        switch (type) {
            case ES_TYPE_PROGRAM:
                return Program.fromJson(jsonObject);

            case ES_TYPE_IDENTIFIER:
                return Identifier.fromJson(jsonObject);

            case ES_TYPE_VAR_DEC:
                return VariableDeclaration.fromJson(jsonObject);

            case ES_TYPE_VAR_DECT:
                return VariableDeclarator.fromJson(jsonObject);

            default:
                throw new CompileFailed("acorn: unknown type: " + type);
        }
    }

    private static Location parseLocation(JSONObject locationObject) {
        Location loc = new Location();
        loc.line = locationObject.getInt("line");
        loc.column = locationObject.getInt("column");

        return loc;
    }

    // ~~~ es tree types
    private static final String
            ES_TYPE_PROGRAM = "Program",
            ES_TYPE_IDENTIFIER = "Identifier",
            ES_TYPE_VAR_DEC = "VariableDeclaration",
            ES_TYPE_VAR_DECT = "VariableDeclarator";
}
