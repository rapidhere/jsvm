package ranttu.rapid.jsvm.test.example;

import com.google.common.collect.ImmutableList;
import ranttu.rapid.jsvm.test.base.JsvmExampleTestBase;

import java.util.List;

/**
 * result = begin + count * step
 * @author rapidhere@gmail.com
 * @version $id: Fibonacci.java, v0.1 2017/5/9 dongwei.dq Exp $
 */
public class Sum extends JsvmExampleTestBase {
    @Override
    protected List<Object[]> args() {
        return ImmutableList.of(
            new Object[] {0, 1, 10},
            new Object[] {-10, 20, 0},
            new Object[] {100, 23, 10000}
        );
    }
}
