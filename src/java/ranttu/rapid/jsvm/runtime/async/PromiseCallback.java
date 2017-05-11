package ranttu.rapid.jsvm.runtime.async;

/**
 * the promise callback
 *
 * @author rapidhere@gmail.com
 * @version $id: JsFunctionObject.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public interface PromiseCallback {
    void call(Object value);
}
