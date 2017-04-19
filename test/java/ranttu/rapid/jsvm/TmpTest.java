/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm;

/**
 * ONLY FOR OUTLINE-USAGE
 *
 * @author rapidhere@gmail.com
 * @version $id: TmpTest.java, v0.1 2017/4/13 dongwei.dq Exp $
 */
public class TmpTest {
    int a = 1;

    TmpTest() {
        invoke(() -> {
            System.out.println(this.a);
        });
    }

    public void invoke(Runnable runnable) {
        runnable.run();
    }

    public void invoke(Object... args) {
        System.out.println(args[1]);
    }
}
