package ranttu.rapid.jsvm.runtime;

import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.runtime.async.FuturePromise;
import ranttu.rapid.jsvm.runtime.async.Promise;
import ranttu.rapid.jsvm.runtime.async.PromiseResultHandler;

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
    abstract public FuturePromise invoke(Object context, Object... args);

    /**
     * the point where the await expression leave current method
     * and enter the async executor
     */
    @SuppressWarnings("unused")
    protected void asyncPoint(FuturePromise promise, JsClosure closure, PromiseResultHandler accept,
                              PromiseResultHandler reject, int asyncPoint) {
        promise.done((error, result) -> entry(closure, accept, reject, asyncPoint + 1, result, error));
    }

    /**
     * invoke the first entry point
     */
    @SuppressWarnings("unused")
    protected Promise invokeEntry(JsClosure closure) {
        return new Promise((accept, reject) -> entry(closure, accept, reject, 0, null, null));
    }


    /**
     * get result or throw
     */
    @SuppressWarnings("unused")
    protected static Object getResultOrThrow(Object error, Object result) throws Throwable {
        if(error != null) {
            throw (Throwable) error;
        } else {
            return result;
        }
    }

    @SuppressWarnings("unused")
    protected static Object wrongEntryPoint() {
        throw new RuntimeException("wrong entry point number");
    }

    /**
     * the entry point
     */
    abstract protected void entry(JsClosure closure0, PromiseResultHandler accept, PromiseResultHandler reject, int entryPoint, Object result, Object error);

    /**
     * constructor is not supported in async function
     */
    @Override
    public JsObjectObject construct(Object... args) {
        return $$.notSupport();
    }
}
