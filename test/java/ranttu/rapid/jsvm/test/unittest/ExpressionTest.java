/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test.unittest;

import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.runtime.JsObjectObject;
import ranttu.rapid.jsvm.test.base.JsvmJunitTestBase;

import java.util.Map;

/**
 * some common and base cases for modules
 *
 * @author rapidhere@gmail.com
 * @version $id: ExpressionTest.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
public class ExpressionTest extends JsvmJunitTestBase {
    public static class ExpressionTestData extends BaseCaseData {
        public String jsSource;
        public Object expected;
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void promise(ExpressionTestData testData) throws Exception {
        String clsName = "PromiseTest";
        JsModule module = loadModule(clsName, testData.jsSource);

        assertEquals(testData.expected, ReflectionUtil.getFieldValue(module, "a"));
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void singleLiteral(ExpressionTestData testData) throws Exception {
        String clsName = "JsModuleTest";
        JsModule module = loadModule(clsName, testData.jsSource);

        assertEquals(testData.expected, ReflectionUtil.getFieldValue(module, "a"));
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void objectExpression(ExpressionTestData testData) throws Exception {
        JsModule module = loadModule("ExpressionLiteralTest", testData.jsSource);
        JsObjectObject object = ReflectionUtil.getFieldValue(module, "a");

        Map<String, Object> expected = $$.cast(testData.expected);
        if (expected == null) {
            return;
        }
        for (String key : expected.keySet()) {
            Object value = expected.get(key);
            assertEquals("property: " + key, value, object.getProperty(key));
        }
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void arrayExpression(ExpressionTestData testData) throws Exception {
        objectExpression(testData);
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void assignmentExpression(ExpressionTestData testData) throws Exception {
        String clsName = "AssignmentExpressionTest";
        JsModule module = loadModule(clsName, testData.jsSource);

        assertEquals(testData.expected, ReflectionUtil.getFieldValue(module, "a"));
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void binaryArithmeticOp(ExpressionTestData testData) throws Exception {
        String clsName = "JsModuleTest";
        JsModule module = loadModule(clsName, testData.jsSource);

        assertEquals(testData.expected, ReflectionUtil.getFieldValue(module, "res"));
    }
}
