package ranttu.rapid.jsvm.runtime.indy;

import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsObjectObject;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * the optimistic javascript call site
 *
 * @author rapidhere@gmail.com
 * @version $id: JsIndyOptimisticCallSite.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
@SuppressWarnings("unused")
public class JsIndyOptimisticCallSite extends JsIndyCallSite {
    public JsIndyOptimisticCallSite(JsIndyType indyType, MethodType type) {
        super(indyType, type);
    }

    @Override
    public void init() {
        switch (indyType) {
            case SET_PROP:
                relink(GUARD_SET_PROPERTY);
                break;
            case GET_PROP:
                relink(GUARD_GET_PROPERTY);
                break;
            case UNBOUNDED_INVOKE:
                relink(GUARD_UNBOUNDED_INVOKE);
                break;
            case CONSTRUCT:
            case BOUNDED_INVOKE:
            default:
                super.init();
        }
    }

    public void relink(MethodHandle handle) {
        setTarget(handle.bindTo(this));
    }


    // ~~~ inner cache
    private Map<Class, MethodHandle> cache = new HashMap<>();

    private MethodHandle setTypeSpecifiedMethodHandle(Class rawClass, String name, Class retType, Class...parTypes)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        MethodHandle ret = cache.get(rawClass);
        if (ret == null) {
           Class clazz = RuntimeCompiling.getCompiled(indyType, rawClass);
           Object handler = clazz.getConstructor(JsIndyOptimisticCallSite.class).newInstance(this);
           ret = lookup.findVirtual(clazz, name, MethodType.methodType(retType, parTypes)).bindTo(handler);
           cache.put(rawClass, ret);
        }
        setTarget(ret.asType(type()));

        return ret;
    }

    // ~~~ set property
    public void guard_SET_PROPERTY(Object context, String name, Object val) throws Throwable {
        if (context instanceof JsObjectObject) {
            jsobj_SET_PROPERTY(context, name, val);
            relink(JSOBJ_SET_PROPERTY);
        } else {
            setTypeSpecifiedMethodHandle(context.getClass(), "SET_PROPERTY",
                void.class, Object.class, String.class, Object.class)

            .invokeExact(context, name, val);
        }
    }

    public void jsobj_SET_PROPERTY(Object context, String name, Object val) throws Throwable{
        if (context instanceof JsObjectObject) {
            ((JsObjectObject) context)
                .setProperty(name, val);
        } else {
            guard_SET_PROPERTY(context, name, val);
        }
    }

    // ~~~ get property
    public Object guard_GET_PROPERTY(Object context, String name) throws Throwable {
        if (context instanceof JsObjectObject) {
            relink(JSOBJ_GET_PROPERTY);
            return jsobj_GET_PROPERTY(context, name);
        } else {
            return setTypeSpecifiedMethodHandle(context.getClass(), "GET_PROPERTY",
                Object.class, Object.class, String.class)

            .invokeExact(context, name);
        }
    }

    public Object jsobj_GET_PROPERTY(Object context, String name) throws Throwable {
        if (context instanceof JsObjectObject) {
            return ((JsObjectObject) context).getProperty(name);
        } else {
            return guard_GET_PROPERTY(context, name);
        }
    }

    // ~~~ unbounded invoke
    public Object guard_UNBOUNDED_INVOKE(Object invoker, Object context, Object...args) throws Throwable {
        if (invoker instanceof JsFunctionObject) {
            relink(JSOBJ_UNBOUNDED_INVOKE);
            return jsobj_UNBOUNDED_INVOKE(invoker, context, args);
        } else {
            Method samMethod = ReflectionUtil.getSingleAbstractMethod(
                invoker.getClass(), type().parameterCount() - 2);

            if (samMethod == null) {
                // TODO
                throw new RuntimeException("not a sam method");
            } else {
                return setTypeSpecifiedMethodHandle(samMethod.getDeclaringClass(), "UNBOUNDED_INVOKE",
                    Object.class, Object.class, Object.class, Object[].class)

                .invoke(invoker, context, args);
            }
        }
    }

    public Object jsobj_UNBOUNDED_INVOKE(Object invoker, Object context, Object...args) throws Throwable {
        if (invoker instanceof JsFunctionObject) {
            return ((JsFunctionObject) invoker).invoke(context, args);
        } else {
            return guard_UNBOUNDED_INVOKE(invoker, context, args);
        }
    }

    // ~~~ construct object
    public Object guard_CONSTRUCT_OBJECT(Object invoker, Object...args) throws Throwable {
        if (invoker instanceof JsFunctionObject) {
            relink(JSOBJ_CONSTRUCT_OBJECT);
            return jsobj_CONSTRUCT_OBJECT(invoker, args);
        } else {
            if (invoker instanceof Class) {
                return setTypeSpecifiedMethodHandle(Class.class, "CONSTRUCT_OBJECT",
                    Object.class, Object[].class)

                .invokeExact(invoker, args);
            } else {
                throw new RuntimeException("cannot new a object");
            }
        }
    }

    public Object jsobj_CONSTRUCT_OBJECT(Object invoker, Object...args) throws Throwable {
        if (invoker instanceof JsFunctionObject) {
            return ((JsFunctionObject) invoker).construct(args);
        } else {
            return guard_CONSTRUCT_OBJECT(invoker, args);
        }
    }



    // ~~~ method handles
    private static MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static MethodHandle GUARD_SET_PROPERTY;
    private static MethodHandle JSOBJ_SET_PROPERTY;
    private static MethodHandle GUARD_GET_PROPERTY;
    private static MethodHandle JSOBJ_GET_PROPERTY;
    private static MethodHandle GUARD_UNBOUNDED_INVOKE;
    private static MethodHandle JSOBJ_UNBOUNDED_INVOKE;
    private static MethodHandle GUARD_CONSTRUCT_OBJECT;
    private static MethodHandle JSOBJ_CONSTRUCT_OBJECT;

    private static void initMh() throws NoSuchMethodException, IllegalAccessException {
        for(Field f: JsIndyOptimisticCallSite.class.getDeclaredFields()) {
            if(f.getType() == MethodHandle.class) {
                String names[] = f.getName().split("_");
                String methodName = names[0].toLowerCase() + "_" + names[1] + "_" + names[2];
                Method method = ReflectionUtil.getMethodWithName(JsIndyOptimisticCallSite.class, methodName);
                MethodType mt = MethodType.methodType(method.getReturnType(), method.getParameterTypes());

                f.set(null, lookup.findVirtual(JsIndyOptimisticCallSite.class, methodName, mt));
            }
        }
    }

    static {
        try {
            initMh();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
