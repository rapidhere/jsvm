package ranttu.rapid.jsvm.runtime.indy;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import ranttu.rapid.jsvm.codegen.ClassNode;
import ranttu.rapid.jsvm.codegen.MethodNode;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.runtime.JsObjectObject;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * the optimistic javascript call site
 *
 * @author rapidhere@gmail.com
 * @version $id: JsIndyOptimisticCallSite.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
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
            default:
                super.init();
        }
    }

    public void relink(MethodHandle handle) {
        setTarget(handle.bindTo(this));
    }

    // ~~~ caches
    private static Map<String, Class> cache = new HashMap<>();

    public Class getCompiled(Class rawClass) {
        return cache.get(indyType.name() + "#" + rawClass.getName());
    }

    public Class putCompiled(Class rawClass, Class compiled) {
        cache.put(indyType.name() + "#" + rawClass.getName(), compiled);
        return compiled;
    }

    // ~~~ set property
    public void guardSetProperty(Object context, String name, Object val) throws Throwable {
        if (context instanceof JsObjectObject) {
            jsObjectSetProperty(context, name, val);
            relink(JSOBJ_SET_PROPERTY);
        } else {
            Class clazz = genSetProperty(null, context.getClass());
            Object setter = clazz.getConstructor(JsIndyOptimisticCallSite.class)
                .newInstance(this);
            MethodHandle mh = lookup.findVirtual(clazz, "SET_PROPERTY",
                MethodType.methodType(void.class, Object.class, String.class, Object.class))
                    .bindTo(setter);
            setTarget(mh);
            mh.invokeExact(context, name, val);
        }
    }

    public void jsObjectSetProperty(Object context, String name, Object val) throws Throwable{
        if (context instanceof JsObjectObject) {
            ((JsObjectObject) context)
                .setProperty(name, val);
        } else {
            guardSetProperty(context, name, val);
        }
    }

    // define helper
    public interface GenInvoke {
        void gen(ClassNode cls, MethodNode method, String className, String methodName);
    }

    public static Class gen(String className, String methodName, GenInvoke genInvoke) {
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
            .astore("cs")
            .ret();

        // exception table
        genInvoke.gen(cls, cls.method(methodName)
            .acc(Opcodes.ACC_PUBLIC), className, methodName);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cls.$.accept(cw);
        byte[] clsBytes = cw.toByteArray();
        $$.printBytecode(clsBytes);

        return $$.UNSAFE.defineAnonymousClass(
            JsIndyOptimisticCallSite.class, clsBytes, null);
    }

    public Class genSetProperty(Class oldClazz, Class newClazz) {
        Class targetClazz = findLCA(oldClazz, newClazz);

        Class ret = getCompiled(targetClazz);
        if(ret != null) {
            return ret;
        }

        // FIXME:
        // this impl is buggy on: if context is a subclass of targetClazz,
        // some field from context that is not belong to targetClazz will fail
        ret = gen("SET_PROPERTY", "SET_PROPERTY", (cls, method, clsName, methodName) -> {
            String methodDesc = $$.getMethodDescriptor(void.class, Object.class, String.class, Object.class);

            method
                .desc(methodDesc)
                .par("this", cls)
                .par("context", Object.class)
                .par("name", String.class)
                .par("val", Object.class);

            LabelNode guardedFailing = new LabelNode();

            // first, cast and try catch
            method
                // test guarded condition
                .load("context")
                .instance_of(targetClazz)
                .jump_if_eq(guardedFailing)
                // name.intern
                .load("name")
                .invoke_virtual($$.getInternalName(String.class), "intern",
                    $$.getMethodDescriptor(String.class))
                // get interned hash code
                .invoke_static($$.getInternalName(System.class), "identityHashCode",
                    $$.getMethodDescriptor(int.class, Object.class));

            // generate field accessor
            Field[] fields = targetClazz.getFields();
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

            // switch table begin
            for (int i = 0;i < fieldSize;i ++) {
                Class fieldType = fields[i].getType();

                method
                    .put_label(fieldLabels[i])
                    .fsame()
                    .load("context")
                    .check_cast($$.getInternalName(targetClazz))
                    .load("val")
                    .check_cast($$.getInternalName(fieldType))
                    .store($$.getInternalName(targetClazz), fields[i].getName(),
                        $$.getDescriptor(fieldType))
                    .ret();
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

            // failing
            method
                // exception - catch
                .put_label(guardedFailing)
                .fsame()
                .load("this")
                .load(clsName, "cs",
                    $$.getDescriptor(JsIndyOptimisticCallSite.class))
                .load("context")
                .load("name")
                .load("val")
                .invoke_virtual(
                    $$.getInternalName(JsIndyOptimisticCallSite.class),
                    "guardSetProperty",
                    $$.getMethodDescriptor(void.class, Object.class, String.class, Object.class))
                .ret();
        });

        putCompiled(targetClazz, ret);
        return ret;
    }

    public static Class findLCA(Class clazzA, Class clazzB) {
        // TODO
        return clazzB;
    }

    // ~~~ method handles
    private static MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static MethodHandle GUARD_SET_PROPERTY;
    private static MethodHandle JSOBJ_SET_PROPERTY;

    private static MethodHandle find(String name, Class retType, Class...pars)
        throws NoSuchMethodException, IllegalAccessException {

        return lookup.findVirtual(JsIndyOptimisticCallSite.class, name,
            MethodType.methodType(retType, pars));
    }

    static {
        try {
            GUARD_SET_PROPERTY = find("guardSetProperty",
                void.class, Object.class, String.class, Object.class);
            JSOBJ_SET_PROPERTY = find("jsObjectSetProperty",
                void.class, Object.class, String.class, Object.class);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
