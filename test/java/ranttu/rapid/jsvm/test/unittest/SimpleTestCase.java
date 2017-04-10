/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test.unittest;

import org.junit.Test;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.test.base.JsvmJunitTestBase;

/**
 * some common and base cases
 *
 * @author rapidhere@gmail.com
 * @version $id: SimpleTestCase.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
public class SimpleTestCase extends JsvmJunitTestBase {
    @Test
    public void test() throws Exception {
        String clsName = "JsModuleTest";

        JsModule module = loadModule(clsName, "let a = 1;");

        assertEquals(clsName, module.getClass().getSimpleName());
        assertEquals(new Integer(1), ReflectionUtil.getFieldValue(module, "a"));
    }
}
