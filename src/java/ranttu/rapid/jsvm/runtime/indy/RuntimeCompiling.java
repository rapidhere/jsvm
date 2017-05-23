package ranttu.rapid.jsvm.runtime.indy;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.common.SystemProperty;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;

import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * the runtime compiling manager
 *
 * @author rapidhere@gmail.com
 * @version $id: RuntimeCompiling.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class RuntimeCompiling {
    /** the singleton */
    public static final RuntimeCompiling theRc = new RuntimeCompiling();

    /**
     * get a sam glue class
     */
    public static Class getSamGlue(Class targetInterface) {
        return theRc.getSamGlueInternal(targetInterface);
    }

    /**
     * get a compiled class
     */
    public static Class getCompiled(JsIndyType type, Class rawClass, Object...extraArgs) {
        return theRc.get(type, rawClass, extraArgs);
    }

    /**
     * define a anounymous class
     */
    public static Class defineAnonymous(Class hostClass, String className, AnonymousClassDefiner definer) {
        if (SystemProperty.UseVAC) {
            return $$.UNSAFE.defineAnonymousClass(hostClass, definer.define(className), null);
        } else {
            return theRc.defineAnonymousClass(hostClass.getClassLoader(), className, definer);
        }
    }

    public interface AnonymousClassDefiner {
        byte[] define(String className);
    }

    // impl
    private Map<String, Class> compiled = new HashMap<>();
    private Map<Class, Class> samGlueCompiled = new HashMap<>();

    private AtomicInteger anonymousClassHashCount = new AtomicInteger(1000000);

    private RuntimeCompiling() {
    }

    private Class defineAnonymousClass(ClassLoader cl, String className, AnonymousClassDefiner definer) {
        int hash = anonymousClassHashCount.incrementAndGet();
        String hashedClassName = className + '$' + hash;
        byte[] bytes = definer.define(hashedClassName);
        return $$.UNSAFE.defineClass(
            hashedClassName, bytes, 0, bytes.length, cl, null);
    }

    private String getKey(JsIndyType type, Class rawClass) {
        return type.name() + '#' + rawClass.getName();
    }

    public Class getSamGlueInternal(Class targetInterface) {
        return samGlueCompiled.computeIfAbsent(targetInterface, this::genGlue);
    }

    public Class get(JsIndyType type, Class rawClass, Object...extraArgs) {
        String key = getKey(type, rawClass);
        Class ret = compiled.get(key);

        if (ret != null) {
            return ret;
        } else {
            switch (type) {
                case SET_PROP:
                    ret = compileSetProperty(rawClass);
                    break;
                case GET_PROP:
                    ret = compileGetProperty(rawClass);
                    break;
                case UNBOUNDED_INVOKE:
                    ret = compileUnboundedInvoke(rawClass);
                    break;
                case CONSTRUCT:
                    ret = compileConstructor(rawClass, (Integer) extraArgs[0]);
                    break;
                case BOUNDED_INVOKE:
                    ret = compileBoundedInvoke(rawClass);
                    break;
                default:
                    $$.notSupport();
            }

            compiled.put(key, ret);
            return ret;
        }
    }

    private Class genGlue(Class targetInterface) {
        Method samMethod = targetInterface.getMethods()[0];
        ClassNode cls = new ClassNode();

        return defineAnonymous(RuntimeCompiling.class, "SAM_GLUE$" + targetInterface.getSimpleName(), (className) -> {
            cls
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_SYNTHETIC, Opcodes.ACC_SUPER)
                .name(className, Object.class)
                .interfaze($$.getInternalName(targetInterface))
                // store field
                .field("function")
                .desc(JsFunctionObject.class);

            // static construct method
            LabelNode guardLabel = new LabelNode();
            cls.method("getGlueInterface")
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_STATIC)
                .desc(targetInterface, Object.class)
                .par("o", Object.class)
                // instanceof
                .load("o")
                .instance_of(JsFunctionObject.class)
                .jump_if_eq(guardLabel)
                .new_class(className)
                .dup()
                .load("o")
                .check_cast($$.getInternalName(JsFunctionObject.class))
                .invoke_special(className, "<init>",
                    $$.getMethodDescriptor(void.class, JsFunctionObject.class))
                .aret()
                // direct return
                .put_label(guardLabel)
                .ffull(new Object[] { $$.getInternalName(Object.class) }, new Object[0])
                .load("o")
                .check_cast($$.getInternalName(targetInterface))
                .aret();


            // init method
            cls.method_init(JsFunctionObject.class)
                .acc(Opcodes.ACC_PUBLIC)
                .par("function", JsFunctionObject.class)
                // call super init
                .load("this")
                .invoke_special($$.getInternalName(Object.class), "<init>",
                    $$.getMethodDescriptor(void.class))
                // store function
                .load("this")
                .load("function")
                .store(className, "function", $$.getDescriptor(JsFunctionObject.class))
                // ret
                .ret();

            // invoke method
            MethodNode method = cls.method(samMethod.getName())
                .acc(Opcodes.ACC_PUBLIC)
                .desc(samMethod.getReturnType(), (Object[]) samMethod.getParameterTypes())
                // get function
                .aload(0)
                .load(className, "function", $$.getDescriptor(JsFunctionObject.class))
                // set context to this
                .aload(0)
                // new args array
                .load_const(samMethod.getParameterCount())
                .anew_array($$.getInternalName(Object.class));
            // load arguments
            for (int i = 0;i < samMethod.getParameterCount();i ++) {
                method
                    .dup()
                    .load_const(i)
                    .aload(i + 1)
                    .aastore();
            }
            // call invoke
            method.invoke_virtual($$.getInternalName(JsFunctionObject.class),
                "invoke", $$.getMethodDescriptor(Object.class, Object.class, Object[].class));
            // ret
            if (samMethod.getReturnType() == void.class) {
                method.pop().ret();
            } else {
                method
                    .check_cast($$.getInternalName(samMethod.getReturnType()))
                    .aret();
            }

            return cls.writeClass(1);
        });
    }


    private interface GenInvoke {
        void gen(ClassNode cls, MethodNode method, String className, String methodName, LabelNode failing);
    }

    private Class gen(String className0, String methodName, MethodType mt, GenInvoke genInvoke) {
        return defineAnonymous(RuntimeCompiling.class, className0, className -> {
            ClassNode cls = new ClassNode();
            cls
                .acc(Opcodes.ACC_PUBLIC, Opcodes.ACC_SYNTHETIC, Opcodes.ACC_SUPER)
                .name(className, Object.class);

            // stored field
            cls.field("cs").desc(JsIndyOptimisticCallSite.class);

            // init method
            cls.method("<init>")
                .acc(Opcodes.ACC_PUBLIC)
                .desc(void.class, JsIndyOptimisticCallSite.class)
                .par("this", cls)
                .par("cs", JsIndyOptimisticCallSite.class)
                // call super init
                .load("this")
                .invoke_special($$.getInternalName(Object.class), "<init>", $$.getMethodDescriptor(void.class))
                // store cs
                .load("this")
                .load("cs")
                .store(className, "cs", $$.getDescriptor(JsIndyOptimisticCallSite.class))
                .ret();

            // specifier gen
            LabelNode failing = new LabelNode();
            MethodNode method = cls.method(methodName)
                .acc(Opcodes.ACC_PUBLIC)
                .desc(mt.toMethodDescriptorString());

            genInvoke.gen(cls, method,
                className, methodName, failing);

            // fall back
            method
                // exception - catch
                .put_label(failing)
                .fsame()
                // put cs on the stack
                .load("this")
                .load(className, "cs",
                    $$.getDescriptor(JsIndyOptimisticCallSite.class));

            // put parameters on stack
            for (int i = 0; i < mt.parameterCount(); i++) {
                method.load(i + 1);
            }

            // fall back to guard point
            method
                .invoke_virtual(
                    $$.getInternalName(JsIndyOptimisticCallSite.class),
                    "guard_" + methodName, mt.toMethodDescriptorString());
            // return guarded result
            if (mt.returnType() == void.class) {
                method.ret();
            } else {
                method.aret();
            }

            return cls.writeClass(1);
        });
    }

    private void guardForExactType(MethodNode method, Class targetClass, LabelNode failing) {
        method
            // test guarded condition
            // context.getClass() == targetClazz
            .load(1)
            .invoke_virtual($$.getInternalName(Object.class),
                "getClass", $$.getMethodDescriptor(Class.class))
            .load_const($$.getType(targetClass))
            .jump_if_acmpne(failing);
    }

    private void guardAsInstance(MethodNode methodNode, Class targetClass, LabelNode failing) {
        methodNode
            .load(1)
            .instance_of(targetClass)
            .jump_if_eq(failing);
    }


    private interface SwitchTableInvoke<T> {
        void gen(T field);
    }

    private Field[] getAllFields(Class targetClazz) {
        List<Field> fields = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        while (true) {
            for(Field f: targetClazz.getDeclaredFields()) {
                if(! visited.contains(f.getName())) {
                    fields.add(f);
                    visited.add(f.getName());
                }
            }

            if (targetClazz == Object.class) {
                break;
            } else {
                targetClazz = targetClazz.getSuperclass();
            }
        }

        return fields.toArray(new Field[fields.size()]);
    }

    private Method[] getAllMethods(Class targetClazz) {
        List<Method> methods = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        while (true) {
            // ignore all generated method
            if(! targetClazz.isSynthetic() && ! targetClazz.getName().contains("/")) {
                for (Method method : targetClazz.getDeclaredMethods()) {
                    if (! method.isSynthetic()
                        && Modifier.isPublic(method.getModifiers())
                        && !visited.contains(method.getName())) {
                        methods.add(method);
                        visited.add(method.getName());
                    }
                }
            }

            for (Class inter: targetClazz.getInterfaces()) {
                for (Method method: getAllMethods(inter)) {
                    if(!visited.contains(method.getName())) {
                        methods.add(method);
                        visited.add(method.getName());
                    }
                }
            }

            Class superClass = targetClazz.getSuperclass();
            if (superClass == null) {
                break;
            } else {
                targetClazz = superClass;
            }
        }

        return methods.toArray(new Method[methods.size()]);
    }

    private String getMethodKey(Method method) {
        return method.getName() + '$' +
            $$.getMethodDescriptor(method.getReturnType(), (Object[]) method.getParameterTypes());
    }

    private Map<String, Class> resolveAllInterfaceMethod(Class targetClazz) {
        Map<String, Class> ret = new HashMap<>();

        while (true) {
            for (Class inter: targetClazz.getInterfaces()) {
                for (Method method: inter.getDeclaredMethods()) {
                    ret.put(getMethodKey(method), inter);
                }
            }

            if (targetClazz == Object.class) {
                break;
            } else {
                targetClazz = targetClazz.getSuperclass();
            }
        }

        return ret;
    }

    private <T extends Member> void nameSwitchTable(MethodNode method, T[] fields, LabelNode fail, boolean defaultToFail, SwitchTableInvoke<T> invoke) {
        // name.hashCode
        method
            .load("name")
            .invoke_virtual($$.getInternalName(Object.class), "hashCode",
                $$.getMethodDescriptor(int.class));

        // generate field accessor
        LabelNode defaultLabel = new LabelNode();
        if(defaultToFail) {
            defaultLabel = fail;
        }

        // generate labels
        Map<Integer, LabelNode> labelMap = new HashMap<>();
        Multimap<Integer, T> fieldMap = ArrayListMultimap.create();

        for (T field : fields) {
            int code = field.getName().hashCode();
            fieldMap.put(code, field);
            if (!labelMap.containsKey(code)) {
                labelMap.put(code, new LabelNode());
            }
        }

        // switch table
        method.lookup_switch(defaultLabel, labelMap);

        // generate switch branches
        for(Map.Entry<Integer, LabelNode> ent: labelMap.entrySet()) {
            int hash = ent.getKey();
            LabelNode switchLabel = ent.getValue();

            // switch entry
            method.put_label(switchLabel).fsame();

            int i = 0;
            Collection<T> currentFields = fieldMap.get(hash);
            LabelNode fallbackLabel = null;
            for(T field: currentFields) {
                if (i != 0) {
                    method.put_label(fallbackLabel).fsame();
                }

                if(i == currentFields.size() - 1) {
                    fallbackLabel = defaultLabel;
                } else {
                    fallbackLabel = new LabelNode();
                }

                // fallback
                method
                    .load("name")
                    .load_const(field.getName())
                    .invoke_virtual($$.getInternalName(Object.class), "equals",
                        $$.getMethodDescriptor(boolean.class, Object.class))
                    .jump_if_eq(fallbackLabel);
                invoke.gen(field);
                i ++;
            }
        }

        if (! defaultToFail) {
            // switch default begin
            // throw new NoSuchFieldException([name])
            method
                .put_label(defaultLabel)
                .fsame()
                .new_class($$.getInternalName(NoSuchFieldException.class))
                .dup()
                .load("name")
                .check_cast($$.getInternalName(String.class))
                .invoke_special($$.getInternalName(NoSuchFieldException.class), "<init>",
                    $$.getMethodDescriptor(void.class, String.class))
                .athrow();
        }
    }

    private void fitArgs(MethodNode method, Executable executable) {
        for (int i = 0;i < executable.getParameterCount();i ++) {
            method.load("args")
                .load_const(i)
                .aaload();

            Class parType = executable.getParameterTypes()[i];
            if (ReflectionUtil.isSingleAbstractMethod(parType)) {
                method.invoke_dynamic_sam_glue(parType);
            } else if (parType.isPrimitive()) {
                unwrapPrimitiveParameter(method, parType);
            } else {
                method.check_cast($$.getInternalName(parType));
            }
        }
    }

    private void unwrapPrimitiveParameter(MethodNode method, Class parType) {
        if (parType == boolean.class) {
            unwrapPrimitive(method, boolean.class, Boolean.class);
        } else if (parType == char.class) {
            unwrapPrimitive(method, char.class, Character.class);
        } else if (parType == byte.class) {
            unwrapPrimitive(method, byte.class, Byte.class);
        } else if (parType == short.class) {
            unwrapPrimitive(method, short.class, Short.class);
        } else if (parType == int.class) {
            unwrapPrimitive(method, int.class, Integer.class);
        } else if (parType == long.class) {
            unwrapPrimitive(method, long.class, Long.class);
        } else if (parType == float.class) {
            unwrapPrimitive(method, float.class, Float.class);
        } else if (parType == double.class) {
            unwrapPrimitive(method, double.class, Double.class);
        }
    }

    private void unwrapPrimitive(MethodNode method, Class ptype, Class wtype) {
        method
            .check_cast($$.getInternalName(wtype))
            .invoke_virtual($$.getInternalName(wtype), ptype.getSimpleName() + "Value",
                $$.getMethodDescriptor(ptype));
    }

    private void wrapReturn(MethodNode method, Method m) {
        Class retType = m.getReturnType();
        if (retType == void.class) {
            method.load_null();
        } else if (retType == boolean.class) {
            wrapPrimitive(method, boolean.class, Boolean.class);
        } else if (retType == char.class) {
            wrapPrimitive(method, char.class, Character.class);
        } else if (retType == byte.class) {
            wrapPrimitive(method, byte.class, Byte.class);
        } else if (retType == short.class) {
            wrapPrimitive(method, short.class, Short.class);
        } else if (retType == int.class) {
            wrapPrimitive(method, int.class, Integer.class);
        } else if (retType == long.class) {
            wrapPrimitive(method, long.class, Long.class);
        } else if (retType == float.class) {
            wrapPrimitive(method, float.class, Float.class);
        } else if (retType == double.class) {
            wrapPrimitive(method, double.class, Double.class);
        }

        // for other non-primitive types, simply do nothing
    }

    private void wrapPrimitive(MethodNode method, Class primitiveType, Class warpType) {
        method.invoke_static($$.getInternalName(warpType), "valueOf",
            $$.getMethodDescriptor(warpType, primitiveType));
    }

    private Class compileBoundedInvoke(Class targetClazz) {
        MethodType mt = MethodType.methodType(Object.class, Object.class, Object.class, Object[].class);

        return gen("BOUNDED_INVOKE", "BOUNDED_INVOKE", mt, (cls, method, clsName, methodName, failing) -> {
            method
                .par("this", cls)
                .par("invoker", Object.class)
                .par("name", Object.class)
                .par("args", Object[].class);

            Map<String, Class> interfaceMethods = resolveAllInterfaceMethod(targetClazz);
            // generate field accessor
            nameSwitchTable(method, getAllMethods(targetClazz), failing, true, (m) -> {
                Class inter = interfaceMethods.get(getMethodKey(m));
                String methodDesc = $$.getMethodDescriptor(m.getReturnType(), (Object[]) m.getParameterTypes());

                if (inter != null) {
                    guardAsInstance(method, inter, failing);
                    method.load("invoker").check_cast($$.getInternalName(inter));
                    fitArgs(method, m);
                    method.invoke_interface($$.getInternalName(inter), m.getName(), methodDesc);
                } else {
                    guardAsInstance(method, m.getDeclaringClass(), failing);
                    method.load("invoker").check_cast($$.getInternalName(m.getDeclaringClass()));
                    fitArgs(method, m);
                    method.invoke_virtual($$.getInternalName(m.getDeclaringClass()), m.getName(), methodDesc);
                }

                wrapReturn(method, m);
                method.aret();
            });
        });
    }

    private Class compileConstructor(Class targetClazz, int numberOfArgs) {
        MethodType mt = MethodType.methodType(Object.class, Object.class, Object[].class);

        return gen("CONSTRUCT_OBJECT", "CONSTRUCT_OBJECT", mt, (cls, method, clsName, methodName, failing) -> {
            method
                .par("this", cls)
                .par("invoker", Object.class)
                .par("args", Object[].class);

            // find constructor
            Constructor con = null;
            for(Constructor c: targetClazz.getConstructors()) {
                if (c.getParameterCount() == numberOfArgs) {
                    con = c;
                    break;
                }
            }
            if(con == null) {
                throw new RuntimeException("cannot find a suitable constructor");
            }

            // guard
            method
                .load("invoker")
                .load_const($$.getType(targetClazz))
                .jump_if_acmpne(failing)
                .new_class($$.getInternalName(targetClazz))
                .dup();

            // put args
            fitArgs(method, con);

            // invoke special
            method.invoke_special($$.getInternalName(targetClazz), "<init>",
                $$.getMethodDescriptor(void.class, (Object[]) con.getParameterTypes()));
            method.aret();
        });
    }


    private Class compileUnboundedInvoke(Class targetInterface) {
        MethodType mt = MethodType.methodType(Object.class, Object.class, Object.class, Object[].class);

        return gen("UNBOUNDED_INVOKE", "UNBOUNDED_INVOKE", mt, (cls, method, clsName, methodName, failing) -> {
            method
                .par("this", cls)
                .par("invoker", Object.class)
                .par("context", Object.class)
                .par("args", Object[].class);

            Method samMethod = targetInterface.getMethods()[0];

            guardAsInstance(method, targetInterface, failing);
            // put args
            method.load("invoker");
            fitArgs(method, samMethod);
            // invoke
            method
                .invoke_interface($$.getInternalName(targetInterface), samMethod.getName(),
                    $$.getMethodDescriptor(samMethod.getReturnType(), (Object[]) samMethod.getParameterTypes()));
            wrapReturn(method, samMethod);
            method.aret();
        });
    }

    private Class compileGetProperty(Class targetClazz) {
        MethodType mt = MethodType.methodType(Object.class, Object.class, String.class);

        return gen("GET_PROPERTY", "GET_PROPERTY", mt, (cls, method, clsName, methodName, failing) -> {
            method
                .par("this", cls)
                .par("context", Object.class)
                .par("name", String.class);

            guardForExactType(method, targetClazz, failing);
            nameSwitchTable(method, getAllFields(targetClazz), failing, false, (field) ->
                method
                    .load("context")
                    .check_cast($$.getInternalName(targetClazz))
                    .load($$.getInternalName(targetClazz), field.getName(),
                        $$.getDescriptor(field.getType()))
                    .aret()
            );
        });
    }


    private Class compileSetProperty(Class targetClazz) {
        MethodType mt = MethodType.methodType(void.class, Object.class, String.class, Object.class);

        return gen("SET_PROPERTY", "SET_PROPERTY", mt, (cls, method, clsName, methodName, failing) -> {
            method
                .par("this", cls)
                .par("context", Object.class)
                .par("name", String.class)
                .par("val", Object.class);

            guardForExactType(method, targetClazz, failing);
            nameSwitchTable(method, getAllFields(targetClazz), failing, false, (field) ->
                method
                    .load("context")
                    .check_cast($$.getInternalName(targetClazz))
                    .load("val")
                    .check_cast($$.getInternalName(field.getType()))
                    .store($$.getInternalName(targetClazz), field.getName(),
                        $$.getDescriptor(field.getType()))
                    .ret()
            );
        });
    }
}
