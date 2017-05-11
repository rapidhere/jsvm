package ranttu.rapid.jsvm.runtime;

import java.util.concurrent.Future;

/**
 * a javascript function object that marked with `async` keyword
 *
 * @author rapidhere@gmail.com
 * @version $id: JsFunctionObject.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
abstract public class JsAsyncFunctionObject extends JsFunctionObject {
    /**
     * invoke and return a java.concurrent.Future Object
     */
    @Override
    abstract public Future invoke(Object context, Object... args);
}
