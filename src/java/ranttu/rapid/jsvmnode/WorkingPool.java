package ranttu.rapid.jsvmnode;

import ranttu.rapid.jsvm.runtime.async.FuturePromise;
import ranttu.rapid.jsvm.runtime.async.Promise;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * the working thread pool
 *
 * @author rapidhere@gmail.com
 * @version $id: WorkingPool.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class WorkingPool {
    // the executor singleton
    private static final WorkingPool workingPool = new WorkingPool();

    // the inner executor
    private ExecutorService executor;

    // the number of working tasks
    private AtomicInteger numberOfTasks = new AtomicInteger();

    //~~~ singleton helpers

    /**
     * get the pool
     */
    public static WorkingPool get() {
        return workingPool;
    }

    /**
     * submit a task and execute in async thread pool, return a future object
     */
    public static FuturePromise submit(Callable task) {
        return get().submitAsyncTask(task);
    }

    /**
     * submit a task and execute in async thread pool, without return value
     */
    public static void submitWithoutReturn(Runnable task) {
        get().submitNoReturn(task);
    }

    // inner method
    private WorkingPool() {
        executor = new ThreadPoolExecutor(10,
            100,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>());
    }

    public void shutdown(boolean now) {
        if (now) {
            executor.shutdownNow();
        } else {
            executor.shutdown();
        }
    }

    public int getTaskCount() {
        return numberOfTasks.get();
    }

    public void submitNoReturn(Runnable task) {
        numberOfTasks.incrementAndGet();
        executor.submit(() -> {
            try {
                task.run();
            } finally {
                numberOfTasks.decrementAndGet();
            }
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    public FuturePromise submitAsyncTask(Callable task) {
        return new Promise((accept, reject)-> {
            numberOfTasks.incrementAndGet();

            executor.submit(() -> {
                try {
                    Object result = task.call();
                    MainLoop.get().submitEvent(accept, result);
                } catch (Exception e) {
                    MainLoop.get().submitEvent(reject, e);
                } finally {
                    numberOfTasks.decrementAndGet();
                }
            });
        });
    }
}
