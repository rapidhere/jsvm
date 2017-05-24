package ranttu.rapid.jsvm.test.testrt;

/**
 * a common object for test usage
 *
 * @author rapidhere@gmail.com
 * @version $id: SomeObject.java, v0.1 2017/5/8 dongwei.dq Exp $
 */
@SuppressWarnings("unused")
public class SomeObject {
    private Object privateField;

    private void privateMethod() {
        System.out.println("in private method");
    }

    public void publicMethod() {
        System.out.println("in public method");
    }

    public Object field1 = 1;
    public Object field2 = 2;
    public Object field3 = 3;
    public Object field4 = 4;

    public double method1(double i) {
        return i + 1;
    }

    public double method2(double i) {
        return i + 2;
    }

    public double method3(double i) {
        return i + 3;
    }

    public double method4(double i) {
        return i + 4;
    }
}
