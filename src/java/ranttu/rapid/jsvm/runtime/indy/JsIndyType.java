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

    GET_PROP(Object.class, Object.class, String.class)

    ;

    private String desc;

    JsIndyType(Class retType, Class...parsType) {
        desc = Type.getMethodDescriptor(retType, parsType);
    }

    public String getDescriptor() {
        return desc;
    }
}
