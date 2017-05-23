package ranttu.rapid.jsvm.runtime.indy;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

/**
 * the base javascript call site
 *
 * @author rapidhere@gmail.com
 * @version $id: JsIndyBaseCallSite.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
abstract public class JsIndyBaseCallSite extends MutableCallSite {
    protected JsIndyType          indyType;
    protected MethodHandles.Lookup contextLookUp;

    public JsIndyBaseCallSite(MethodHandles.Lookup lookup, JsIndyType indyType, MethodType type) {
        super(type);
        this.indyType = indyType;
        this.contextLookUp = lookup;
    }

    /**
     * init the call site
     */
    abstract public void init();
}
