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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: JsvmExampleTestBase.java, v0.1 2017/5/9 dongwei.dq Exp $
 */
abstract public class JsvmBenchMarkTestBase extends JsvmExampleTestBase {
    @Test
    public void run() throws Exception {
        String source = getTestSource("testres/benchmark/");
        String titlePrefix = getClass().getSimpleName() + "::";

        SampleCase jsvmCase = new SampleCase() {
            {
                title = titlePrefix + "jsvm: ";
            }
            private JsFunctionObject entry;

            @Override
            protected void preapre() {
                JsModule module = loadModule(getClass().getSimpleName() + "_Benchmark", source);
                entry = ReflectionUtil.getFieldValue(module, "entry");
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
            protected void preapre() throws Throwable {
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
                engine.eval(source);
                invoker = $$.cast(engine);
            }

            @Override
            protected void run(Object... args) throws Throwable {
                invoker.invokeFunction("entry", args);
            }
        };

        try {
            sample(100, jsvmCase, nashornCase);
        } catch (Throwable e) {
            fail(e);
        }
    }

    private void sample(int sampleCnt, SampleCase...cases0) throws Throwable {
        List<SampleCase> cases = Arrays.asList(cases0);
        for (int i = 0;i < sampleCnt;i ++) {
            System.out.println("turn: " + (i + 1) + "/" + sampleCnt);

            Collections.shuffle(cases);
            for (SampleCase sampleCase: cases) {
                sampleCase.sample();
            }
        }

        cases.forEach((c) -> c.calcAndReport(sampleCnt));
    }


    abstract protected static class SampleCase {
        public String title;
        public List<Long> sampleTimes = new ArrayList<>();

        public void sample() throws Throwable {
            Object[] parameters = new Object[] { new SomeObject(), new AnotherObject(),
                new YetAnotherObject(), new MathWrapper()};

            preapre();
            long start = System.currentTimeMillis();
            run(parameters);
            sampleTimes.add(System.currentTimeMillis() - start);
        }

        public void calcAndReport(int sampleCnt) {
            sampleTimes.sort(null);

            int dropCnt = (int)(sampleCnt * 0.05);
            if(dropCnt < 1) {
                dropCnt = 1;
            }

            long timeSum = 0;
            long tot = sampleCnt - dropCnt * 2;
            for(int i = dropCnt;i + dropCnt < sampleTimes.size();i ++) {
                timeSum += sampleTimes.get(i);
            }
            double avg = (double)timeSum / (double)tot;

            double varTot = 0.0;
            for(int i = dropCnt;i + dropCnt< sampleTimes.size();i ++) {
                double tmp = ((double)sampleTimes.get(i) - avg);
                varTot += tmp * tmp;
            }
            double var = Math.sqrt(varTot / (double)tot);

            System.out.println(String.format("%s: avg: %.4f ms; %.4fms",
                title, avg, var));
        }

        abstract protected void preapre() throws Throwable;
        abstract protected void run(Object...args) throws Throwable;
    }

    // not used
    @Override
    protected List<Object[]> args() {
        return null;
    }
}
