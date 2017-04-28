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

    // ret, invoker, name
    BOUNDED_INVOKE(Object.class, Object.class, Object.class),

    // ret, invoker, context
    UNBOUNDED_INVOKE(Object.class, Object.class, Object.class),

    CONSTRUCT(Object.class, Object.class)
    ;

    private Class   retType;
    private Class[] parsType;

    JsIndyType(Class retType, Class... parsType) {
        this.retType = retType;
        this.parsType = parsType;
    }


    public String getDescriptor(Class... clazz) {
        Class[] pars = new Class[clazz.length + parsType.length];
        System.arraycopy(parsType, 0, pars, 0, parsType.length);
        System.arraycopy(clazz, 0, pars, parsType.length, clazz.length);
        return Type.getMethodDescriptor(retType, pars);
    }
}
