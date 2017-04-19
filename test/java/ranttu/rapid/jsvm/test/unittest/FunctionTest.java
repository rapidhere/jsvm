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
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.test.base.JsvmJunitTestBase;

/**
 * function test cases
 *
 * @author rapidhere@gmail.com
 * @version $id: FunctionTest.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
@RunWith(DataProviderRunner.class)
public class FunctionTest extends JsvmJunitTestBase {
    public static class FunctionTestData extends BaseCaseData {
        public String jsSource;
        public Object expected;
        public Object[] parameters;
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void baseCase(FunctionTestData testData) {
        JsModule module = loadModule("FunctionTest", testData.jsSource);

        JsFunctionObject func = ReflectionUtil.getFieldValue(module, "func");
        if(testData.parameters == null) {
            testData.parameters = new Object[0];
        }

        Object result = func.invoke(testData.parameters);

        assertEquals(jsValueOf(testData.expected), result);
    }
}
