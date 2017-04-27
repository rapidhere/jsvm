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
public class TmpTest extends Base{
    protected int a;
    public TmpTest() {}

    private class Inner {
        private class InnerInner {
            InnerInner() {
                System.out.println(b);
            }
        }
    }
}

class Base {
    protected int b;
}