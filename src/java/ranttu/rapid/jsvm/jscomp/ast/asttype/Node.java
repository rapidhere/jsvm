/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.asttype;

import ranttu.rapid.jsvm.jscomp.ast.Location;

/**
 * @author rapidhere@gmail.com
 * @version $id: Node.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public interface Node {
    Location getStartLocation();

    Location getEndLocation();
}
