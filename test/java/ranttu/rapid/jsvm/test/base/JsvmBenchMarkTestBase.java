package ranttu.rapid.jsvm.test.base;

import org.junit.Test;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.test.testrt.AnotherObject;
import ranttu.rapid.jsvm.test.testrt.MathWrapper;
import ranttu.rapid.jsvm.test.testrt.SomeObject;
import ranttu.rapid.jsvm.test.testrt.YetAnotherObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: JsvmExampleTestBase.java, v0.1 2017/5/9 dongwei.dq Exp $
 */
abstract public class JsvmBenchMarkTestBase extends JsvmExampleTestBase {
    @Test
    public void run() throws Exception {
        String source = getTestSource("testres/benchmark/");

        Printable nashornPrint = (o) -> System.err.println("N " + o);
        Printable jsvmPrint = (o) -> System.err.println("J " + o);

        // jsvm object
        JsModule module = sampleOnce("jsvm compile",
            () -> loadModule(getClass().getSimpleName() + "_Benchmark", source));
        JsFunctionObject jsvmEntry = ReflectionUtil.getFieldValue(module, "entry");

        // nashorn object
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        sampleOnce("nashorn compile", () -> engine.eval(source));
        Invocable invoker = $$.cast(engine);

        System.out.println(sampleOnce("nashorn run", () ->
            invoker.invokeFunction("entry", new SomeObject(), new AnotherObject(),
                new YetAnotherObject(), new MathWrapper(), nashornPrint)));
        System.out.println(sampleOnce("jsvm run", () ->
            jsvmEntry.invoke(this, new SomeObject(), new AnotherObject(),
                new YetAnotherObject(), new MathWrapper(), jsvmPrint)));
    }

    private <T> T sampleOnce(String title, SampleInvoke<T> invoke) {
        long start = System.currentTimeMillis();
        try {
            return invoke.run();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        } finally {
            long end = System.currentTimeMillis();
            double ms = (double)(end - start) / 1000.0;

            System.out.println(getClass().getSimpleName() +": " + title + " " + ms + " s");
        }
    }

    public interface Printable {
        @SuppressWarnings("unused")
        void print(Object o);
    }

    protected interface SampleInvoke<T> {
        T run() throws Throwable;
    }

    // not used
    @Override
    protected List<Object[]> args() {
        return null;
    }
}
