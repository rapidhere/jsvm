/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test.unittest;

import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
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

        Object result = func.invoke(null, testData.parameters);

        assertEquals(testData.expected, result);
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void invokeCase(FunctionTestData testData) {
        JsModule module = loadModule("FunctionTest", testData.jsSource);

        Object a = ReflectionUtil.getFieldValue(module, "a");
        assertEquals(testData.expected, a);
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void boundedCase(FunctionTestData testData) {
        JsModule module = loadModule("BoundedFunctionTest", testData.jsSource);

        Object a = ReflectionUtil.getFieldValue(module, "a");
        assertEquals(testData.expected, a);
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void functionExpCase(FunctionTestData testData) {
        JsModule module = loadModule("FunctionExpressionTest", testData.jsSource);

        JsFunctionObject func = ReflectionUtil.getFieldValue(module, "func");
        if(testData.parameters == null) {
            testData.parameters = new Object[0];
        }

        Object result = func.invoke(null, testData.parameters);

        assertEquals(testData.expected, result);
    }
}
