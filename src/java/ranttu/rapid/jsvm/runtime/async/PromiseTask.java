package ranttu.rapid.jsvm.runtime.async;

/**
 * the the promise task object
 *
 * @author rapidhere@gmail.com
 * @version $id: PromiseResultHandler.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public interface PromiseTask {
    void invoke(PromiseResultHandler accept, PromiseResultHandler reject);
}
