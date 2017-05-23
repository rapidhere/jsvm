package ranttu.rapid.jsvm.test.testrt;

import ranttu.rapid.jsvm.runtime.async.JsExecutorService;
import ranttu.rapid.jsvm.runtime.async.Promise;

import java.util.Random;

/**
 * only for test usage
 */
public class ThreadAsyncManager {
    public static final int val = new Random().nextInt(100);

    @SuppressWarnings("unused")
    public Promise instanceTask() {
        return JsExecutorService.submit(() -> {
            Thread.sleep(1000);
            return val;
        });
    }

    @SuppressWarnings("unused")
    public Promise instanceStaticTask(Number integer) {
        return JsExecutorService.submit(() -> {
            Thread.sleep(1000);
            return integer.intValue();
        });
    }
}
