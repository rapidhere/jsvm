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
        while(f(new Runnable() {
            @Override
            public void run() {

            }
        })) {
            System.out.print("233");
        }
    }

    public boolean f(Runnable run) {
        int i = 1;
        System.out.println(i);
        run.run();
        return false;
    }
}
