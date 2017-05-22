package ranttu.rapid.jsvm.runtime.indy;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.common.$$;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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
     * get a compiled class
     */
    public static Class getCompiled(JsIndyType type, Class rawClass) {
        return theRc.get(type, rawClass);
    }

    // impl
    private Map<String, Class> compiled = new HashMap<>();

    private RuntimeCompiling() {
    }

    private String getKey(JsIndyType type, Class rawClass) {
        return type.name() + '#' + rawClass.getName();
    }

    public Class get(JsIndyType type, Class rawClass) {
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
                default:
                    $$.notSupport();
            }

            compiled.put(key, ret);
            return ret;
        }
    }

    private interface GenInvoke {
        void gen(ClassNode cls, MethodNode method, String className, String methodName, LabelNode failing);
    }

    private Class gen(String className, String methodName, MethodType mt, GenInvoke genInvoke) {
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
        for (int i = 0;i < mt.parameterCount();i ++) {
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

        // compute
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cls.$.accept(cw);
        byte[] clsBytes = cw.toByteArray();
        $$.printBytecode(className, clsBytes);

        return $$.UNSAFE.defineAnonymousClass(
            JsIndyOptimisticCallSite.class, clsBytes, null);
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


    private interface SwitchTableInvoke {
        void gen(LabelNode label, Field field);
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

    private void internNameAndSwitchTable(MethodNode method, Class targetClazz, SwitchTableInvoke invoke) {
        // name.intern
        method
            .load("name")
            .invoke_virtual($$.getInternalName(String.class), "intern",
                $$.getMethodDescriptor(String.class))
            // get interned hash code
            .invoke_static($$.getInternalName(System.class), "identityHashCode",
                $$.getMethodDescriptor(int.class, Object.class));

        // generate field accessor
        Field[] fields = getAllFields(targetClazz);
        int[] fieldHashCodes = new int[fields.length];
        LabelNode[] fieldLabels = new LabelNode[fields.length];
        LabelNode defaultLabel = new LabelNode();
        int fieldSize = fields.length;

        for (int i = 0;i < fieldSize;i ++) {
            LabelNode l = new LabelNode();
            fieldLabels[i] = l;
            fieldHashCodes[i] = System.identityHashCode(
                fields[i].getName());
        }
        // switch table
        method.lookup_switch(defaultLabel, fieldHashCodes, fieldLabels);

        for(int i = 0;i < fieldSize;i ++) {
            invoke.gen(fieldLabels[i], fields[i]);
        }

        // switch default begin
        // throw new NoSuchFieldException([name])
        method
            .put_label(defaultLabel)
            .fsame()
            .new_class($$.getInternalName(NoSuchFieldException.class))
            .dup()
            .load("name")
            .invoke_special($$.getInternalName(NoSuchFieldException.class), "<init>",
                $$.getMethodDescriptor(void.class, String.class))
            .athrow();
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

            method
                .load("invoker")
                .instance_of(targetInterface)
                .jump_if_eq(failing)
                .load("invoker");

            // put args
            for (int i = 0;i < samMethod.getParameterCount();i ++) {
                method.load("args")
                    .load_const(i)
                    .aaload();
            }

            // invoke
            method
                .invoke_interface($$.getInternalName(targetInterface), samMethod.getName(),
                    $$.getMethodDescriptor(samMethod.getReturnType(), (Object[]) samMethod.getParameterTypes()));
            if (samMethod.getReturnType() == void.class) {
                method.load_null();
            }
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
            internNameAndSwitchTable(method, targetClazz, (label, field) ->
                method
                    .put_label(label)
                    .fsame()
                    .load("context")
                    .check_cast($$.getInternalName(targetClazz))
                    .load($$.getInternalName(targetClazz), field.getName(),
                        $$.getDescriptor(field.getType()))
                    .aret());
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
            internNameAndSwitchTable(method, targetClazz, (label, field) ->
                method
                    .put_label(label)
                    .fsame()
                    .load("context")
                    .check_cast($$.getInternalName(targetClazz))
                    .load("val")
                    .check_cast($$.getInternalName(field.getType()))
                    .store($$.getInternalName(targetClazz), field.getName(),
                        $$.getDescriptor(field.getType()))
                    .ret());
        });
    }
}
