/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test;

import ranttu.rapid.jsvm.runtime.JsFunctionObject;

import java.util.concurrent.Callable;

/**
 * ONLY FOR OUTLINE-USAGE
 *
 * @author rapidhere@gmail.com
 * @version $id: TmpTest.java, v0.1 2017/4/13 dongwei.dq Exp $
 */
public class TmpTest implements Callable {
    private JsFunctionObject jsFunctionObject;

    public TmpTest(Object obj) {
        jsFunctionObject = (JsFunctionObject) obj;
    }

    public Object call() {
        int a = 100, b = 200;
        int c = a + b;
        return jsFunctionObject.invoke(null);
    }
}
