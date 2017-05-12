package ranttu.rapid.jsvm.runtime.async;

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

    /**
     * get the executor
     */
    public static JsExecutorService getExecutor() {
        return jsExecutorService;
    }

    /**
     * submit a task with accept callback and error callback
     */
    public static void submit(Callable task, PromiseResultHandler accept, PromiseResultHandler error) {
        getExecutor().submitTask(task, accept, error);
    }

    /**
     * submit a task and get a Promise object for the task
     */
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
            try {
                Object result = task.call();
                accept.call(result);
            } catch (Throwable e) {
                error.call(e);
            }
        });
    }

    public Promise submitTask(Callable task) {
        return new Promise((accept, error) -> submitTask(task, accept, error));
    }
}
