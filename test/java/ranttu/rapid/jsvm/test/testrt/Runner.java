package ranttu.rapid.jsvm.test.testrt;

import java.util.concurrent.Callable;

/**
 * for test usage only
 */
@SuppressWarnings("unused")
public class Runner {
    public Object run(Callable run) throws Throwable{
        return run.call();
    }
}
