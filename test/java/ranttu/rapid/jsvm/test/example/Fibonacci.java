/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test.example;

import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.runner.RunWith;
import ranttu.rapid.jsvm.test.base.JsvmExampleTestBase;

import java.util.List;

/**
 * f[n] = f[n - 1] + f[n - 2]
 * f[1] = 1
 * f[0] = 0
 *
 * @author rapidhere@gmail.com
 * @version $id: Fibonacci.java, v0.1 2017/5/9 dongwei.dq Exp $
 */
@RunWith(DataProviderRunner.class)
public class Fibonacci extends JsvmExampleTestBase {
    @Override
    protected List<Object[]> args() {
        return ImmutableList.of(
            new Object[] {0},
            new Object[] {1},
            new Object[] {2},
            new Object[] {3},
            new Object[] {20},
            new Object[] {25},
            new Object[] {30}
        );
    }
}
