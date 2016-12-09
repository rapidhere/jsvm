/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast;

/**
 * the location of a ast
 *
 * @author rapidhere@gmail.com
 * @version $id: Location.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class Location {
    /** line number, index from 1 */
    private int line;

    /** column, index from 0 */
    private int column;

    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
