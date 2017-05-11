package ranttu.rapid.jsvm.runtime.async;

import ranttu.rapid.jsvm.common.$$;

import javax.annotation.Nonnull;
import java.util.concurrent.*;

/**
 * the promise object
 *
 * @author rapidhere@gmail.com
 * @version $id: Promise.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class Promise implements FuturePromise {
    private PromiseCallback successCallback, failedCallback;
    private Object result, exception;

    private boolean resolved = false;

    public Promise(PromiseTask task) {
        task.invoke(
            (obj) -> report(obj, null),
            (obj) -> report(null, obj));
    }

    private void report(Object result, Object exception) {
        resolved = true;
        if(result != null) {
            this.result = result;
        } else {
            this.exception = exception;
        }

        resolve();
    }

    @Override
    public Promise then(PromiseCallback callback) {
        successCallback = callback;
        resolve();
        return this;
    }

    @Override
    public Promise error(PromiseCallback callback) {
        failedCallback = callback;
        resolve();
        return this;
    }

    private void resolve() {
        if(isDone()) {
            if(result != null && successCallback != null) {
                successCallback.call(result);
            } else if(exception != null && failedCallback != null) {
                failedCallback.call(exception);
            }
        }
    }

    @Override
    public boolean isDone() {
        return resolved;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    //~~~ not supported
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public Object get(long timeout, @Nonnull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return $$.notSupport();
    }
}
