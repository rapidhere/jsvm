package ranttu.rapid.jsvm.runtime.indy;

import java.lang.invoke.MethodType;

/**
 * the optimistic javascript call site
 *
 * @author rapidhere@gmail.com
 * @version $id: JsIndyOptimisticCallSite.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class JsIndyOptimisticCallSite extends JsIndyBaseCallSite {
    public JsIndyOptimisticCallSite(JsIndyType indyType, MethodType type) {
        super(indyType, type);
    }

    @Override
    public void init() {

    }
}
