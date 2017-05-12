package ranttu.rapid.jsvm.runtime.async;

import java.util.concurrent.Future;

/**
 * promise object that implements future
 *
 * @author rapidhere@gmail.com
 * @version $id: JsFunctionObject.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public interface FuturePromise extends Future<Object> {
    /**
     * called when the task is executing successfully
     */
    Promise then(PromiseCallback callback);

    /**
     * called the the task is failed
     */
    Promise error(PromiseCallback callback);
}
