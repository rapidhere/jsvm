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
 * some common and base cases for modules
 *
 * @author rapidhere@gmail.com
 * @version $id: ModuleTestCase.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
@RunWith(DataProviderRunner.class)
public class LiteralTestCase extends JsvmJunitTestBase {
    public static class LiteralCaseData extends BaseCaseData {
        public String jsSource;
        public Object expected;
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void singleVar(LiteralCaseData testCase) throws Exception {
        String clsName = "JsModuleTest";
        JsModule module = loadModule(clsName, testCase.jsSource);

        assertEquals(clsName, module.getClass().getSimpleName());
        assertEquals(testCase.expected, ReflectionUtil.getFieldValue(module, "a"));
    }
}
