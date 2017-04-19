package ranttu.rapid.jsvm.codegen.ir;


import ranttu.rapid.jsvm.common.$$;

/**
 * literal
 *
 * @author rapidhere@gmail.com
 * @version $id: IrLoadLocal.java, v0.1 2017/4/18 dongwei.dq Exp $
 */
public class IrLiteral extends IrNode {
    public LiteralType type;
    public Object value;

    public IrLiteral(LiteralType type, Object obj) {
        this.type = type;
        this.value = obj;
    }

    public static IrLiteral of(String str) {
        return new IrLiteral(LiteralType.STRING, str);
    }

    public static IrLiteral of(int i) {
        return new IrLiteral(LiteralType.INTEGER, i);
    }

    public static IrLiteral of(double d) {
        return new IrLiteral(LiteralType.DOUBLE, d);
    }

    public String getString() {
        return $$.cast(value);
    }

    public int getInt() {
        return $$.cast(value);
    }

    public double getDouble() {
        return $$.cast(value);
    }
}
