/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

/**
 * a js module
 *
 * @author rapidhere@gmail.com
 * @version $id: JsModule.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
abstract public class JsModule extends JsRuntime {
    /** the module instance field name */
    public static final String FIELD_MODULE_NAME = "module";

    // ~~~ added js runtime public things
    protected final JsFunctionObject Object = JsRuntime.Object;

    /** cannot create directly */
    protected JsModule() {}
}
