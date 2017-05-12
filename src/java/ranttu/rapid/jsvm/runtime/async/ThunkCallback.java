package ranttu.rapid.jsvm.runtime.async;

/**
 * the thunk callback
 *
 * @author rapidhere@gmail.com
 * @version $id: ThunkCallback.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public interface ThunkCallback {
    void call(Object error, Object result);
}
