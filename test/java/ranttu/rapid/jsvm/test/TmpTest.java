/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test;

/**
 * ONLY FOR OUTLINE-USAGE
 *
 * @author rapidhere@gmail.com
 * @version $id: TmpTest.java, v0.1 2017/4/13 dongwei.dq Exp $
 */
public class TmpTest {
    public TmpTest() {
        if (f(0)) {
            f(1);
        } else {
            f(2);
        }
    }

    public boolean f(Object o) {
        return Boolean.valueOf(o.toString());
    }
}
