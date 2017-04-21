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
    private static final class ObjectClass extends JsFunctionObject {
        @Override
        public Object invoke(Object $this, Object... args) {
            if(args.length == 0) {
                return construct();
            } else {
                return args[1];
            }
        }

        public JsObjectObject construct(Object... args) {
            JsFunctionObject obj = new ObjectClass();
            if(args.length > 0) {
                invoke(obj, args);
            }
            obj.setProperty("__proto__", getProperty("prototype"));

            return obj;
        }
    }
    public static final JsFunctionObject Object = new ObjectClass();
}
