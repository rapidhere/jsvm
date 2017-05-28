package ranttu.rapid.jsvmnode;

import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.async.PromiseResultHandler;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * MainLoop
 *
 * @author rapidhere@gmail.com
 * @version $id: MainLoop.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
public class MainLoop {
    private static final MainLoop theLoop = new MainLoop();

    /**
     * the getter
     */
    public static MainLoop get() {
        return theLoop;
    }

    // impl
    private MainLoop() {
    }

    // the event queue
    private BlockingQueue<MainLoopEvent> eventQueue = new LinkedBlockingQueue<>();

    public void start(String entryClassName) {
        submitEvent(entryClassName);

        // start loop
        int exitCode;
        while (true) {
            int ret = singleLoop();

            if (ret != Integer.MIN_VALUE) {
                exitCode = ret;
                break;
            }
        }

        WorkingPool.get().shutdown(exitCode != 0);
        System.exit(exitCode);
    }

    private int singleLoop() {
        // wait for a event
        MainLoopEvent e;
        try {
            // no more tasks to do
            if (eventQueue.isEmpty() && WorkingPool.get().getTaskCount() == 0) {
                return 0;
            }

            // get a mew task
            e = eventQueue.take();

            // class load task
            if (e.handle instanceof String) {
                Class.forName((String) e.handle);
            }
            // promise result handle
            else if(e.handle instanceof PromiseResultHandler) {
                PromiseResultHandler handler = (PromiseResultHandler) e.handle;
                handler.call(e.args[0]);
            }
            // js function object
            else if(e.handle instanceof JsFunctionObject) {
                Object $this = e.args[0];
                Object[] rest = Arrays.copyOfRange(e.args, 1, e.args.length);
                ((JsFunctionObject) e.handle).invoke($this, rest);
            }
            else {
                throw new RuntimeException("unknown event handle: " + e.handle);
            }

            return Integer.MIN_VALUE;
        } catch (InterruptedException exc) {
            System.err.println("interrupted, exit now: ");
            exc.printStackTrace();
            return -1;
        } catch (Exception exc) {
            System.err.println("unknown error: ");
            exc.printStackTrace();
            return -255;
        }
    }

    public void submitEvent(Object handle, Object...args) {
        MainLoopEvent e = new MainLoopEvent();
        e.handle = handle;
        e.args = args;

        eventQueue.add(e);
    }
}
