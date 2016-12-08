/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.astnode;

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
}
