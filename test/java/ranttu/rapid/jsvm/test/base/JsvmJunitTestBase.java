/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test.base;

import org.junit.Assert;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;
import ranttu.rapid.jsvm.jscomp.comp.Compiler;
import ranttu.rapid.jsvm.jscomp.parser.AcornJSParser;
import ranttu.rapid.jsvm.jscomp.parser.Parser;
import ranttu.rapid.jsvm.runtime.JsModule;

import java.io.ByteArrayOutputStream;

/**
 * the base junit test facility
 *
 * @author rapidhere@gmail.com
 * @version $id: JsvmJunitTestBase.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
abstract public class JsvmJunitTestBase extends Assert {

    // the class loader
    protected ByteArrayClassLoader byteArrayClassLoader = new ByteArrayClassLoader();

    /**
     * compile the source to byte array
     * @param source the source to compile
     * @return compiled byte code
     */
    protected byte[] compileSource(String source) {
        Parser parser = new AcornJSParser();
        AbstractSyntaxTree ast = parser.parse(source);

        Compiler compiler = new Compiler(ast);
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

        try {
            compiler.compile(output);
        } catch (Exception e) {
            fail("failed to compile source", e);
        }

        return output.toByteArray();
    }

    /**
     * load the source to a java class
     * @param source the source to load
     * @return loaded class
     */
    protected Class<? extends JsModule> loadSource(String source) {
        byte[] bytecodes = compileSource(source);

        return byteArrayClassLoader.loadClass(JsModule.class, bytecodes);
    }

    /**
     * load the source a JsModule instance
     * @param source the source to load
     * @return loaded module
     */
    protected JsModule loadModule(String source) {
        Class<? extends JsModule> moduleClass = loadSource(source);

        try {
            return ReflectionUtil.getStaticValue(moduleClass, JsModule.FIELD_MODULE_NAME);
        } catch (Exception e) {
            return fail(e);
        }
    }

    // ~~~ assertion helpers
    protected static <T> T fail(String message, Throwable e) {
        e.printStackTrace();
        fail(message);

        return $$.shouldNotReach();
    }

    protected static <T> T fail(Throwable e) {
        fail(null, e);

        return $$.shouldNotReach();
    }
}
