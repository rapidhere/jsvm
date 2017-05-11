package ranttu.rapid.jsvm.runtime.async;

import java.util.concurrent.Future;

/**
 * promise object that implements future
 *
 * @author rapidhere@gmail.com
 * @version $id: JsFunctionObject.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public interface FuturePromise extends Future<Object> {
    Promise then(PromiseCallback callback);

    Promise error(PromiseCallback callback);
}
