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
    protected int a;

    class Inner {
        Inner() {
            System.out.println(a);
        }
    }

    public TmpTest() {
        new Inner();
    }

    public static void main(String args[]) {
        new TmpTest();
    }
}