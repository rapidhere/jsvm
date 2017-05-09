/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

import ranttu.rapid.jsvm.common.$$;

/**
 * runtime defines
 *
 * @author rapidhere@gmail.com
 * @version $id: JsRuntime.java, v0.1 2017/4/21 dongwei.dq Exp $
 */
abstract public class JsRuntime {
    // ~~~ Object()
    protected static final class ObjectClass extends JsFunctionObject {
        @Override
        public Object invoke(Object $this, Object... args) {
            if (args.length == 0) {
                return construct();
            } else {
                return args[0];
            }
        }

        @Override
        public JsObjectObject construct(Object... args) {
            JsObjectObject obj = new JsObjectObject();
            if (args.length > 0) {
                invoke(obj, args);
            }
            obj.setProperty("__proto__", getProperty("prototype"));

            return obj;
        }
    }
    public static final JsFunctionObject Object = new ObjectClass();

    // ~~~ Function()
    // NOTE: not for directly usage
    protected static final class FunctionClass extends JsFunctionObject {
        @Override
        public Object invoke(Object $this, Object... args) {
            throw new RuntimeException("not supported");
        }

        @Override
        public JsObjectObject construct(Object... args) {
            throw new RuntimeException("not supported");
        }
    }
    public static final JsFunctionObject Function = new FunctionClass();

    // ~~~ helpers

    /**
     * cast a object to a boolean value
     */
    @SuppressWarnings("unused")
    public static int castToBooleanValue(Object bool) {
        boolean result = false;
        if(bool != null) {
            if(bool instanceof Boolean) {
                result = (Boolean) bool;
            } else if(bool instanceof Number) {
                result = ((Number)bool).intValue() != 0;
            } else if(bool instanceof String) {
                result = ((String)bool).length() != 0;
            } else {
                result = true;
            }
        }

        return result ? 1 : 0;
    }

    // ~~~ arithmetic operators
    @SuppressWarnings("unused")
    public static Object ADD(Object a, Object b) {
        double da = $$.cast(a, Number.class).doubleValue();
        double db = $$.cast(b, Number.class).doubleValue();

        return da + db;
    }

    @SuppressWarnings("unused")
    public static Object SUBTRACT(Object a, Object b) {
        double da = $$.cast(a, Number.class).doubleValue();
        double db = $$.cast(b, Number.class).doubleValue();

        return da - db;
    }

    @SuppressWarnings("unused")
    public static Object STRONG_EQUAL(Object a, Object b) {
        if (a instanceof Number && b instanceof Number) {
            return ((Number) a).doubleValue() == ((Number) b).doubleValue();
        } else {
            return false;
        }
    }

    // ~~~ make functions here
    static {
        JsObjectObject objProto = new JsObjectObject();
        JsObjectObject funcProto = new JsObjectObject();

        objProto.setProperty("constructor", Object);
        Object.setProperty("prototype", objProto);
        Object.setProperty("__proto__", funcProto);

        funcProto.setProperty("__proto__", objProto);
        funcProto.setProperty("constructor", Function);
        Function.setProperty("prototype", funcProto);
        Function.setProperty("__proto__", funcProto);
    }
}
