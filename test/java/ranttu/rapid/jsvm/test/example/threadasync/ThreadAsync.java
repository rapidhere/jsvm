package ranttu.rapid.jsvm.test.example.threadasync;

import org.junit.Test;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.runtime.async.Promise;
import ranttu.rapid.jsvm.test.base.JsvmExampleTestBase;

import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: Fibonacci.java, v0.1 2017/5/9 dongwei.dq Exp $
 */
public class ThreadAsync extends JsvmExampleTestBase {
    @Test
    @Override
    public void run() throws Exception {
        String source = getTestSource();
        JsModule module = loadModule("ThreadAsyncTest", source);

        Promise promise = ReflectionUtil.getFieldValue(module, "promise");

        // get result should equals
        int ret = (int) promise.get();
        assertEquals(ThreadAsyncManager.val, ret);

        // check `then` result
        Object result = null;
        int count = 10;
        while(result == null && count > 0) {
            result = ReflectionUtil.getFieldValue(module, "result");
            Thread.sleep(100);
            count --;
        }

        assertEquals(ThreadAsyncManager.val, result);
    }

    // not used
    @Override
    protected List<Object[]> args() {
        return null;
    }
}
