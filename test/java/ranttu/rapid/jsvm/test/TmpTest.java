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

    int a = 0;
    public TmpTest(Object obj) throws Throwable {
        func(() -> {
           return a;
        });
    }

    public void func(Callable callable) throws Exception {
        callable.call();
    }

    public Object call() {
        return null;
    }
}
