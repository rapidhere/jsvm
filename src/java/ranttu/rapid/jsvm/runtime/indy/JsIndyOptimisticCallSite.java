package ranttu.rapid.jsvm.runtime.indy;

import com.google.common.collect.Maps;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsObjectObject;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * the optimistic javascript call site
 *
 * @author rapidhere@gmail.com
 * @version $id: JsIndyOptimisticCallSite.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
@SuppressWarnings("unused")
public class JsIndyOptimisticCallSite extends JsIndyCallSite {

    public JsIndyOptimisticCallSite(MethodHandles.Lookup lookup, JsIndyType indyType, MethodType type) {
        super(lookup, indyType, type);
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
                relink(GUARD_CONSTRUCT_OBJECT);
                break;
            case BOUNDED_INVOKE:
                relink(GUARD_BOUNDED_INVOKE);
                break;
            default:
                super.init();
        }
    }

    public void relink(MethodHandle handle) {
        setTarget(handle.bindTo(this));
    }


    // ~~~ inner cache
    private Map<Class, MethodHandle> cache = new HashMap<>();

    private MethodHandle setTypeSpecifiedMethodHandle(Class rawClass, String name, MethodType mt, Object...extraArgs)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        MethodHandle ret = cache.get(rawClass);
        if (ret == null) {
           Class clazz = RuntimeCompiling.getCompiled(indyType, rawClass, extraArgs);
           Object handler = clazz.getConstructor(JsIndyOptimisticCallSite.class).newInstance(this);
           ret = lookup.findVirtual(clazz, name, mt).bindTo(handler);
           cache.put(rawClass, ret);
        }
        setTarget(ret);

        return ret;
    }

    // ~~~ set property
    public void guard_SET_PROPERTY(Object context, Object name, Object val) throws Throwable {
        if (context instanceof JsObjectObject) {
            jsobj_SET_PROPERTY(context, name, val);
            relink(JSOBJ_SET_PROPERTY);
        } else {
            setTypeSpecifiedMethodHandle(context.getClass(),
                "SET_PROPERTY",
                MethodType.methodType(void.class, Object.class, Object.class, Object.class))

            .invokeExact(context, name, val);
        }
    }

    public void jsobj_SET_PROPERTY(Object context, Object name, Object val) throws Throwable{
        if (context instanceof JsObjectObject) {
            ((JsObjectObject) context)
                .setProperty(name, val);
        } else {
            guard_SET_PROPERTY(context, name, val);
        }
    }

    // ~~~ get property
    public Object guard_GET_PROPERTY(Object context, Object name) throws Throwable {
        if (context instanceof JsObjectObject) {
            relink(JSOBJ_GET_PROPERTY);
            return jsobj_GET_PROPERTY(context, name);
        } else {
            return setTypeSpecifiedMethodHandle(context.getClass(),
                "GET_PROPERTY",
                MethodType.methodType(Object.class, Object.class, Object.class))

            .invokeExact(context, name);
        }
    }

    public Object jsobj_GET_PROPERTY(Object context, Object name) throws Throwable {
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
            Method samMethod = ReflectionUtil.getSingleAbstractMethod(invoker.getClass());

            if (samMethod == null) {
                // TODO
                throw new RuntimeException("not a sam method");
            } else {
                return setTypeSpecifiedMethodHandle(samMethod.getDeclaringClass(), "UNBOUNDED_INVOKE",
                    MethodType.methodType(Object.class, Object.class, Object.class, Object[].class))

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
                // for a call site, the number of args is always the same
                return setTypeSpecifiedMethodHandle((Class) invoker, "CONSTRUCT_OBJECT",
                    MethodType.methodType(Object.class, Object.class, Object[].class), args.length)

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

    // ~~~ bounded invoke
    // cached methods
    private Map<Class, Set<String>> classMethods = Maps.newHashMap();

    public Object guard_BOUNDED_INVOKE(Object invoker, Object name, Object...args) throws Throwable {
        if (invoker instanceof JsObjectObject) {
            relink(JSOBJ_BOUNDED_INVOKE);
            return jsobj_BOUNDED_INVOKE(invoker, name, args);
        } else {
            Set<String> methods = classMethods.get(invoker.getClass());
            if(methods == null) {
                methods = new HashSet<>();
                for (Method m: invoker.getClass().getMethods()) {
                    methods.add(m.getName());
                }
                classMethods.put(invoker.getClass(), methods);
            }

            if (! methods.contains(name)) {
                throw new NoSuchMethodException((String) name);
            }

            return setTypeSpecifiedMethodHandle(invoker.getClass(),
                "BOUNDED_INVOKE",
                MethodType.methodType(Object.class, Object.class, Object.class, Object[].class))

            .invokeExact(invoker, name, args);
        }
    }

    // use to cache reflection result
    private MethodHandle cachedSamMh = null;
    private Class cachedBoundedInvokeType = null;

    public Object jsobj_BOUNDED_INVOKE(Object invoker, Object name, Object...args) throws Throwable {
        if (invoker instanceof JsObjectObject) {
            JsObjectObject obj = (JsObjectObject) invoker;
            Object method = ((JsObjectObject) invoker).getProperty(name);

            if (method instanceof JsFunctionObject) {
                return ((JsFunctionObject)method).invoke(invoker, args);
            } else {
                // if type changed cache is not valid
                if (cachedBoundedInvokeType == null || ! cachedBoundedInvokeType.isInstance(invoker)) {
                    cachedSamMh = null;
                }

                // redefine cache
                if (cachedSamMh == null) {
                    Method samMethod = ReflectionUtil.getSingleAbstractMethod(invoker.getClass());
                    if (samMethod == null) {
                        throw new RuntimeException("not a sam method");
                    }

                    cachedSamMh = MethodHandles.publicLookup()
                        .findVirtual(samMethod.getDeclaringClass(), samMethod.getName(),
                            MethodType.methodType(samMethod.getReturnType(), samMethod.getParameterTypes()));
                }

                // use cache to invoke
                return cachedSamMh.invokeExact(invoker, args);
            }
        } else {
            return guard_BOUNDED_INVOKE(invoker, name, args);
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

    private static MethodHandle GUARD_BOUNDED_INVOKE;
    private static MethodHandle JSOBJ_BOUNDED_INVOKE;

    private static void initMh() throws NoSuchMethodException, IllegalAccessException {
        for(Field f: JsIndyOptimisticCallSite.class.getDeclaredFields()) {
            if(f.getType() == MethodHandle.class && !Objects.equals(f.getName(), "cachedSamMh")) {
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
