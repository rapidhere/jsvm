/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test;

import java.util.concurrent.Callable;

/**
 * ONLY FOR OUTLINE-USAGE
 *
 * @author rapidhere@gmail.com
 * @version $id: TmpTest.java, v0.1 2017/4/13 dongwei.dq Exp $
 */
public class TmpTest implements Callable {

    public TmpTest(Object obj) {
        if (Boolean.valueOf("true")) {
            throw new RuntimeException();
        } else {
            return;
        }
    }

    public Object call() {
        return null;
    }
}
