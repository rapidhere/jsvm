package ranttu.rapid.jsvm.test.unittest;


import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsModule;
import ranttu.rapid.jsvm.test.base.JsvmJunitTestBase;

/**
 * @author rapidhere@gmail.com
 * @version $id: MiscTest.java, v0.1 2017/5/8 dongwei.dq Exp $
 */
public class MiscTest extends JsvmJunitTestBase {
    public static class MiscCaseData extends BaseCaseData {
        public String jsSource;
        public Object expected;
    }

    @Test
    @UseDataProvider("yamlDataProvider")
    public void expSuite(MiscCaseData caseData) {
        JsModule module = loadModule("Test", caseData.jsSource);
        JsFunctionObject func = ReflectionUtil.getFieldValue(module, "f");

        try {
            func.invoke(this);
        } catch (Throwable e) {
            assertEquals(e.getClass().getSimpleName(), caseData.expected);
            e.printStackTrace();
        }
    }
}
