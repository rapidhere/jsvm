/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

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
