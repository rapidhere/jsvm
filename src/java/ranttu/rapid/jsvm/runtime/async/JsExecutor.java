package ranttu.rapid.jsvm.runtime.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * the promise object
 *
 * @author rapidhere@gmail.com
 * @version $id: JsFunctionObject.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class JsExecutor extends ThreadPoolExecutor {
    public static final ExecutorService executor = new JsExecutor();

    protected JsExecutor() {
        super(10,
            100,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>());
    }
}
