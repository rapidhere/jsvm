/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;
import ranttu.rapid.jsvm.jscomp.comp.Compiler;
import ranttu.rapid.jsvm.jscomp.parser.AcornJSParser;
import ranttu.rapid.jsvm.jscomp.parser.Parser;
import ranttu.rapid.jsvm.runtime.JsModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * the base junit test facility
 *
 * @author rapidhere@gmail.com
 * @version $id: JsvmJunitTestBase.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
@RunWith(DataProviderRunner.class)
abstract public class JsvmJunitTestBase extends Assert {
    /**
     * compile the source to byte array
     */
    protected Map<String, byte[]> compileSource(String className, String source, String suffix) {
        Parser parser = new AcornJSParser();
        AbstractSyntaxTree ast = parser.parse(source);

        Compiler compiler = new Compiler(ast);

        try {
            return compiler.compile(className + suffix);
        } catch (Exception e) {
            return fail("failed to compile source", e);
        }
    }

    /**
     * load the source to a java class
     */
    protected Class<? extends JsModule> loadSource(String className, String source) {
        String suffix = "$" + System.currentTimeMillis() +
            (int) Math.floor(Math.random() * 100);

        Map<String, byte[]> ret = compileSource(className, source, suffix);

        Class<? extends JsModule> topClass = null;
        for (Map.Entry<String, byte[]> r : ret.entrySet()) {
            byte[] bytes = r.getValue();
            Class current;
            current = $$.UNSAFE.defineClass(
                r.getKey(), bytes, 0, bytes.length,
                getClass().getClassLoader(), null);

            if (r.getKey().equals(className + suffix)) {
                topClass = current;
            }
        }

        return topClass;
    }

    /**
     * load the source a JsModule instance
     */
    protected JsModule loadModule(String className, String source) {
        Class<? extends JsModule> moduleClass = loadSource(className, source);

        try {
            return ReflectionUtil.getStaticValue(moduleClass, JsModule.FIELD_MODULE_NAME);
        } catch (Exception e) {
            return fail(e);
        }
    }

    // ~~~ assertion helpers
    protected static <T> T fail(String message, Throwable e) {
        if(e != null) {
            e.printStackTrace(System.err);
            fail(message + e.toString());
        } else {
            fail(message);
        }

        return $$.shouldNotReach();
    }

    protected static <T> T fail(Throwable e) {
        fail(null, e);

        return $$.shouldNotReach();
    }

    // ~~~ data provider
    protected static class BaseCaseData {
        public String description;
        public boolean skip = false;

        public String toString() {
            return description;
        }
    }

    @SuppressWarnings("unused")
    @DataProvider(format = "%m[%i]: %p[0]")
    public static List<List<Object>> yamlDataProvider(FrameworkMethod method) throws IOException {
        Class<? extends BaseCaseData> dataClass = $$
            .cast(method.getMethod().getParameterTypes()[0]);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<? extends BaseCaseData> data = mapper.readValue(getTestResource(method), mapper
            .getTypeFactory().constructCollectionType(List.class, dataClass));

        List<List<Object>> result = new ArrayList<>();

        data.stream().filter(d -> !d.skip).forEach(d -> {
            ArrayList l = new ArrayList();
            l.add(d);
            result.add(l);
        });

        return result;
    }

    private static InputStream getTestResource(FrameworkMethod method) {
        Class clazz = method.getMethod().getDeclaringClass();
        String className = clazz.getSimpleName();
        String methodName = method.getName();

        return clazz.getClassLoader().getResourceAsStream(
            "testres/" + className + "/" + methodName + ".yaml");
    }
}
