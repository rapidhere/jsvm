package ranttu.rapid.jsvm.runtime.indy;

import jdk.nashorn.internal.codegen.types.Type;

/**
 * invoke dynamic call type
 *
 * @author rapidhere@gmail.com
 * @version $id: GenerateBytecodePass.java, v0.1 2017/4/6 dongwei.dq Exp $
 */
public enum JsIndyType {
    SET_PROP(void.class, Object.class, String.class, Object.class),

    GET_PROP(Object.class, Object.class, String.class),

    INVOKE(Object.class, Object[].class),
    ;

    private String desc;
    private Class retType;

    JsIndyType(Class retType, Class...parsType) {
        desc = Type.getMethodDescriptor(retType, parsType);
        this.retType = retType;
    }

    public String getDescriptor() {
        return desc;
    }

    public String getDescriptor(Class...clazz) {
        Class[] pars = new Class[clazz.length + 1];
        pars[0] = Object.class;
        System.arraycopy(clazz, 0, pars, 1, clazz.length);
        return Type.getMethodDescriptor(retType, pars);
    }
}
