/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.Location;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

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

    public BaseAstNode(JSONObject jsonObject) {
        startLocation = parseLocation(jsonObject.getJSONObject("loc").getJSONObject("start"));
        endLocation = parseLocation(jsonObject.getJSONObject("loc").getJSONObject("end"));
    }

    @Override
    public Location getStartLocation() {
        return startLocation;
    }

    @Override
    public Location getEndLocation() {
        return endLocation;
    }

    private static Location parseLocation(JSONObject locationObject) {
        return new Location(locationObject.getInt("line"), locationObject.getInt("column"));
    }
}
