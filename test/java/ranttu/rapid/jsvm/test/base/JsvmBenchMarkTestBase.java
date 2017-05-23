package ranttu.rapid.jsvm.test.base;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.common.SystemProperty;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.test.testrt.AnotherObject;
import ranttu.rapid.jsvm.test.testrt.MathWrapper;
import ranttu.rapid.jsvm.test.testrt.SomeObject;
import ranttu.rapid.jsvm.test.testrt.YetAnotherObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: JsvmExampleTestBase.java, v0.1 2017/5/9 dongwei.dq Exp $
 */
abstract public class JsvmBenchMarkTestBase extends JsvmExampleTestBase {
    private static int RUN_TIMES = 20;

    @Test
    public void run() throws Exception {
        if(SystemProperty.IgnoreBenchMark) {
            return ;
        }

        String source = getTestSource("testres/benchmark/");
        String titlePrefix = getClass().getSimpleName() + "::";

        SampleCase jsvmCase = new SampleCase() {
            {
                title = titlePrefix + "jsvm: ";
            }
            private JsFunctionObject entry;

            @Override
            public void preapre() {
                try {
                    SystemProperty.UseOptimisticCallSite = true;
                    JsModule module = loadModule(getClass().getSimpleName() + "_Benchmark", source);
                    entry = ReflectionUtil.getFieldValue(module, "entry");
                } finally {
                    SystemProperty.UseOptimisticCallSite = true;
                }
            }

            @Override
            protected void run(Object... args) {
                entry.invoke(this, args);
            }
        };
        SampleCase nashornCase = new SampleCase() {
            {
                title = titlePrefix + "nashorn: ";
            }

            Invocable invoker;

            @Override
            public void preapre() throws Throwable {
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
                engine.eval(source);
                invoker = $$.cast(engine);
            }

            @Override
            protected void run(Object... args) throws Throwable {
                invoker.invokeFunction("entry", args);
            }
        };
        SampleCase jsvmNoOptimisticCase = new SampleCase() {
            {
                title = titlePrefix + "jsvm-nop: ";
            }
            private JsFunctionObject entry;

            @Override
            public void preapre() throws Throwable {
                try {
                    SystemProperty.UseOptimisticCallSite = false;
                    JsModule module = loadModule(getClass().getSimpleName() + "_Benchmark", source);
                    entry = ReflectionUtil.getFieldValue(module, "entry");
                } finally {
                    SystemProperty.UseOptimisticCallSite = true;
                }
            }

            @Override
            public void run(Object... args) throws Throwable {
                entry.invoke(this, args);
            }
        };
        SampleCase rhinoCase = new SampleCase() {
            {
                title = titlePrefix + "rhino: ";
            }
            private Function f;
            private Context ctx;
            private Scriptable scope;

            @Override
            public void preapre() throws Throwable {
                ctx = Context.enter();
                scope = ctx.initStandardObjects();
                ctx.evaluateString(scope, source, "<dummy>", 1, null);
                f = (Function) scope.get("entry", scope);
            }

            @Override
            protected void run(Object... args) throws Throwable {
                f.call(ctx, scope, scope, args);
            }

            @Override
            protected void exit() {
                Context.exit();
            }
        };

        try {
            sample(10, jsvmCase, nashornCase, jsvmNoOptimisticCase, rhinoCase);
        } catch (Throwable e) {
            fail(e);
        }
    }

    private void sample(int sampleCnt, SampleCase...cases0) throws Throwable {
        List<SampleCase> cases = Arrays.asList(cases0);
        for (int i = 0;i < sampleCnt;i ++) {
            // System.out.println("turn: " + (i + 1) + "/" + sampleCnt);

            Collections.shuffle(cases);
            for (SampleCase sampleCase: cases) {
                sampleCase.preapre();
                for(int j = 0;j < RUN_TIMES;j ++) {
                    sampleCase.sample(j);
                }
                sampleCase.exit();
            }
        }

        for (SampleCase sampleCase: cases) {
            sampleCase.report(sampleCnt);
        }
    }


    abstract protected static class SampleCase {
        public String title;
        public List<Long> sampleTimes = new ArrayList<>();

        public SampleCase() {
            for(int i = 0;i < RUN_TIMES;i ++) {
                sampleTimes.add(0L);
            }
        }

        public void sample(int i) throws Throwable {
            Object[] parameters = new Object[] { new SomeObject(), new AnotherObject(),
                new YetAnotherObject(), new MathWrapper()};

            long start = System.currentTimeMillis();
            run(parameters);
            long tot = sampleTimes.get(i) + (System.currentTimeMillis() - start);
            sampleTimes.set(i, tot);
        }

        public void report(int sampleCnt) {
            List<Double> result = new ArrayList<>();

            for(long t: sampleTimes) {
                result.add((double)t / (double)sampleCnt);
            }

            long timeSum = 0;
            long tot = result.size();
            for (double sampleTime : result) {
                timeSum += sampleTime;
            }
            double avg = (double)timeSum / (double)tot;

            double varTot = 0.0;
            for (double sampleTime : result) {
                double tmp = (sampleTime - avg);
                varTot += tmp * tmp;
            }
            double var = Math.sqrt(varTot / (double)tot);

            System.out.println(title + " ======");
            for(int i = 0;i < result.size();i ++) {
                System.out.println(
                    String.format("turn %d: %.4fms", i, result.get(i)));
            }
            System.out.println(String.format("avg: %.4f ms; %.4fms", avg, var));
        }

        abstract public void preapre() throws Throwable;
        abstract protected void run(Object...args) throws Throwable;

        protected void exit() {

        }
    }

    // not used
    @Override
    protected List<Object[]> args() {
        return null;
    }
}
