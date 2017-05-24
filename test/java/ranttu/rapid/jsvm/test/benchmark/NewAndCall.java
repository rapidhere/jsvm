package ranttu.rapid.jsvm.test.benchmark;

import org.junit.Ignore;
import ranttu.rapid.jsvm.test.base.JsvmBenchMarkTestBase;

/**
 * @author rapidhere@gmail.com
 * @version $id: Fibonacci.java, v0.1 2017/5/9 dongwei.dq Exp $
 */
@Ignore
public class NewAndCall extends JsvmBenchMarkTestBase {
    @Override
    protected String prepareForJsvm(String source) {
        return "import * as Runner from 'ranttu.rapid.jsvm.test.testrt.Runner';\n"
            + source;
    }

    @Override
    protected String prepareForRhino(String source) {
        return "var Runner = Packages.ranttu.rapid.jsvm.test.testrt.Runner;\n"
            + source;
    }

    @Override
    protected String prepareForNashorn(String source) {
        return "var Runner = Java.type('ranttu.rapid.jsvm.test.testrt.Runner');\n"
            + source;
    }
}
