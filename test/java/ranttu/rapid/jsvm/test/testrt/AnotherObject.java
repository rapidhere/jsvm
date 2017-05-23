package ranttu.rapid.jsvm.test.testrt;

/**
 * a common object for test usage
 *
 * @author rapidhere@gmail.com
 * @version $id: SomeObject.java, v0.1 2017/5/8 dongwei.dq Exp $
 */
@SuppressWarnings("unused")
public class AnotherObject {
    private Object privateField;

    private void privateMethod() {
        System.out.println("in private method");
    }

    public void publicMethod() {
        System.out.println("in public method");
    }

    public Object field1 = 5;
    public Object field2 = 6;
    public Object field3 = 7;
    public Object field4 = 8;
}
