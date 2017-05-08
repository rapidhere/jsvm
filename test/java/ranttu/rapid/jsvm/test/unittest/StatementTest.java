/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test.unittest;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.test.base.JsvmJunitTestBase;

/**
 * @author rapidhere@gmail.com
 * @version $id: StatementTest.java, v0.1 2017/5/8 dongwei.dq Exp $
 */
@RunWith(DataProviderRunner.class)
public class StatementTest extends JsvmJunitTestBase {
    public static class StatementTestData extends BaseCaseData {
        public String jsSource;
        public Object expected;
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void ifStatement(StatementTestData testData) {
        JsModule module = loadModule("IfTestCase", testData.jsSource);
        Object ret = ReflectionUtil.getFieldValue(module, "a");

        assertEquals(testData.expected, ret);
    }
}
