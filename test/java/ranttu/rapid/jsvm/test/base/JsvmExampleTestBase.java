/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test.base;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsModule;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: JsvmExampleTestBase.java, v0.1 2017/5/9 dongwei.dq Exp $
 */
abstract public class JsvmExampleTestBase extends JsvmJunitTestBase {
    @Test
    public void run() throws Exception {
        // load source
        String source = getTestSource();

        // jsvm object
        JsModule module = loadModule(getClass().getSimpleName() + "_Test", source);
        JsFunctionObject function = ReflectionUtil.getFieldValue(module, "entry");

        // nashorn object
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(source);
        Invocable invoker = $$.cast(engine);

        for (Object[] arg : args()) {
            Object jsvmResult = function.invoke(this, arg);
            Object nashornResult = invoker.invokeFunction("entry", arg);

            if(nashornResult instanceof Number) {
                assertEquals("arg: " + Arrays.toString(arg),
                    $$.cast(nashornResult, Number.class).doubleValue(),
                    $$.cast(jsvmResult, Number.class).doubleValue(), 0.1);
            } else {
                assertEquals("arg: " + Arrays.toString(arg), jsvmResult, nashornResult);
            }
        }
    }

    abstract protected List<Object[]> args();

    protected String getTestSource() throws IOException {
        Class clazz = getClass();
        String className = clazz.getSimpleName();

        InputStream stream = clazz.getClassLoader().getResourceAsStream("testres/example/" + className + ".js");
        return IOUtils.toString(stream, "UTF8");
    }
}
