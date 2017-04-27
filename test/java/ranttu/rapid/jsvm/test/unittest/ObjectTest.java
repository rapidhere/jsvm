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
 * @author rapidhere@gmail.com
 * @version $id: ObjectTest.java, v0.1 2017/4/27 dongwei.dq Exp $
 */
@RunWith(DataProviderRunner.class)
public class ObjectTest extends JsvmJunitTestBase {
    public static class ObjectTestData extends BaseCaseData {
        public String jsSource;
        public Object expected;
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void instanceOf(ObjectTestData testData) {
        String source = "function f() {" + testData.jsSource + "}";
        JsModule module = loadModule("InstanceOfTest", source);
        JsFunctionObject func = ReflectionUtil.getFieldValue(module, "f");

        assertEquals(testData.expected, func.invoke(module));
    }
}
