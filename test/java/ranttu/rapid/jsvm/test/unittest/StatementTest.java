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
import ranttu.rapid.jsvm.runtime.JsAsyncFunctionObject;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.runtime.JsObjectObject;
import ranttu.rapid.jsvm.runtime.async.FuturePromise;
import ranttu.rapid.jsvm.test.base.JsvmJunitTestBase;

import java.lang.reflect.Field;

/**
 * @author rapidhere@gmail.com
 * @version $id: StatementTest.java, v0.1 2017/5/8 dongwei.dq Exp $
 */
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

    @Test
    @UseDataProvider("yamlDataProvider")
    public void exportStatement(StatementTestData testData) throws Exception {
        JsModule module = loadModule("ExportTestCase", testData.jsSource);

        Field fieldA = module.getClass().getDeclaredField("a");
        Object ret = fieldA.get(module);

        if (ret instanceof JsFunctionObject) {
            assertEquals(testData.expected, $$.cast(ret, JsFunctionObject.class).invoke(this));
        } else {
            assertEquals(testData.expected, ret);
        }
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void importStatement(StatementTestData testData) throws Exception {
        JsModule module = loadModule("ImportTestCase", testData.jsSource);

        Field fieldA = module.getClass().getDeclaredField("a");
        Object ret = fieldA.get(module);

        assertEquals(testData.expected, ret);
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void tryCatch(StatementTestData testData) throws Exception {
        JsModule module = loadModule("TryCatchTest", testData.jsSource);

        JsFunctionObject function = ReflectionUtil.getFieldValue(module, "f");

        Object result = function.invoke(this, (Runnable) () -> {
            throw new RuntimeException("hahaha");
        });
        assertTrue(result instanceof Throwable);
        assertEquals("hahaha", $$.cast(result, Throwable.class).getMessage());

        if(result instanceof JsObjectObject) {
            assertEquals("hahaha",
                $$.cast(result, JsObjectObject.class).getProperty("message"));
        }
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void AsyncAwait(StatementTestData testData) throws Exception {
        JsModule module = loadModule("AsyncAwaitTestCase", testData.jsSource);

        JsAsyncFunctionObject function = ReflectionUtil.getFieldValue(module, "asyncFunc");
        FuturePromise promise = function.invoke(this);

        assertEquals(testData.expected, promise.get());
    }
}
