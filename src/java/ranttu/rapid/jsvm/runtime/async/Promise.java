package ranttu.rapid.jsvm.runtime.async;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * the promise object
 *
 * @author rapidhere@gmail.com
 * @version $id: Promise.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class Promise implements FuturePromise {
    private PromiseCallback successCallback, failedCallback;
    private Object result, exception;

    private volatile boolean resolved = false;
    private Thread currentThread;

    public Promise(PromiseTask task) {
        currentThread = Thread.currentThread();

        task.invoke(
            (obj) -> report(obj, null),
            (obj) -> report(null, obj));
    }

    /**
     * report the result and exception
     *
     * and unpark the probably blocked waiting thread
     */
    private void report(Object result, Object exception) {
        resolved = true;
        if(result != null) {
            this.result = result;
        } else {
            this.exception = exception;
        }

        // unblock currentThread
        LockSupport.unpark(currentThread);

        resolve();
    }

    /**
     * @see FuturePromise#then(PromiseCallback)
     */
    @Override
    public Promise then(PromiseCallback callback) {
        successCallback = callback;
        resolve();
        return this;
    }

    /**
     * @see FuturePromise#error(PromiseCallback)
     */
    @Override
    public Promise error(PromiseCallback callback) {
        failedCallback = callback;
        resolve();
        return this;
    }

    /**
     * try to resolve the final result to the then/error handler
     */
    private void resolve() {
        if(isDone()) {
            if(result != null && successCallback != null) {
                successCallback.call(result);
            } else if(exception != null && failedCallback != null) {
                failedCallback.call(exception);
            }
        }
    }

    /**
     * @see FuturePromise#isDone()
     */
    @Override
    public boolean isDone() {
        return resolved;
    }

    /**
     * @see FuturePromise#get()
     */
    @Override
    public Object get() throws InterruptedException, ExecutionException {
        if(! resolved) {
            waitForResolved(false, 0L);
        }

        return getResultOrThrow();
    }

    /**
     * @see FuturePromise#get(long, TimeUnit)
     */
    @Override
    public Object get(long timeout, @Nonnull TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {

        if(! resolved) {
            waitForResolved(true, unit.toNanos(timeout));
        }

        if(! resolved) {
            throw new TimeoutException();
        } else {
            return getResultOrThrow();
        }
    }

    /**
     * get the result on success, or throw exception
     */
    private Object getResultOrThrow() throws ExecutionException {
        if (result != null) {
            return result;
        } else {
            throw new ExecutionException((Throwable) exception);
        }
    }

    /**
     * wait for the result resolved
     */
    private void waitForResolved(boolean timed, long nano) {
        long deadline = 0;
        if(timed) {
            deadline = nano + System.nanoTime();
        }

        while (! resolved) {
            if(timed) {
                LockSupport.parkUntil(deadline);
            } else {
                LockSupport.park();
            }

            if(timed && deadline <= System.nanoTime()) {
                break;
            }
        }
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
}
