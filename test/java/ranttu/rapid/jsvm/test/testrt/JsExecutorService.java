package ranttu.rapid.jsvm.test.testrt;

import ranttu.rapid.jsvm.runtime.async.Promise;
import ranttu.rapid.jsvm.runtime.async.PromiseResultHandler;

import java.util.concurrent.*;

/**
 * the promise object
 *
 * @author rapidhere@gmail.com
 * @version $id: JsExecutorService.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class JsExecutorService {
    // the executor singleton
    private static final JsExecutorService jsExecutorService = new JsExecutorService();

    // the inner executor
    private ExecutorService executor;

    //~~~ singleton helpers

    public static JsExecutorService getExecutor() {
        return jsExecutorService;
    }

    public static void submit(Callable task, PromiseResultHandler accept, PromiseResultHandler error) {
        getExecutor().submitTask(task, accept, error);
    }

    public static Promise submit(Callable task) {
        return getExecutor().submitTask(task);
    }

    // inner method
    private JsExecutorService() {
        executor = new ThreadPoolExecutor(10,
            100,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>());
    }

    public void submitTask(Callable task, PromiseResultHandler accept, PromiseResultHandler error) {
        executor.submit(() -> {
            Object result;
            try {
                result = task.call();
            } catch (Throwable e) {
                error.call(e);
                return;
            }

            accept.call(result);
        });
    }

    public Promise submitTask(Callable task) {
        return new Promise((accept, error) -> submitTask(task, accept, error));
    }
}
