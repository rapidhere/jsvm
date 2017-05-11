/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime.indy;

import jdk.internal.org.objectweb.asm.*;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.common.ReflectionUtil;
import ranttu.rapid.jsvm.runtime.JsFunctionObject;
import ranttu.rapid.jsvm.runtime.JsObjectObject;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * a invoke-dynamic call site that is mutable
 *
 * @author rapidhere@gmail.com
 * @version $id: JsIndyBindingPoint.java, v0.1 2017/4/17 dongwei.dq Exp $
 */
public class JsIndyCallSite extends MutableCallSite {
    private JsIndyType          indyType;

    private static MethodHandle SET_PROP;
    private static MethodHandle GET_PROP;
    private static MethodHandle INVOKE;
    private static MethodHandle BOUNDED_INVOKE;
    private static MethodHandle CONSTRUCT;

    private static Unsafe UNSAFE;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);

            SET_PROP = MethodHandles.lookup().findStatic(JsIndyCallSite.class, "setProperty",
                MethodType.methodType(void.class, Object.class, String.class, Object.class));

            GET_PROP = MethodHandles.lookup().findStatic(JsIndyCallSite.class, "getProperty",
                MethodType.methodType(Object.class, Object.class, String.class));

            INVOKE = MethodHandles
                .lookup()
                .findStatic(JsIndyCallSite.class, "invoke",
                    MethodType.methodType(Object.class, Object.class, Object.class, Object[].class))
                .asVarargsCollector(Object[].class);

            BOUNDED_INVOKE = MethodHandles
                .lookup()
                .findStatic(JsIndyCallSite.class, "boundedInvoke",
                    MethodType.methodType(Object.class, Object.class, Object.class, Object[].class))
                .asVarargsCollector(Object[].class);

            CONSTRUCT = MethodHandles
                .lookup()
                .findStatic(JsIndyCallSite.class, "construct",
                    MethodType.methodType(Object.class, Object.class, Object[].class));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public JsIndyCallSite(JsIndyType indyType, MethodType type) {
        super(type);
        this.indyType = indyType;
    }

    public void init() {
        switch (indyType) {
            case SET_PROP:
                setTarget(SET_PROP);
                break;
            case GET_PROP:
                setTarget(GET_PROP);
                break;
            case UNBOUNDED_INVOKE:
                setTarget(INVOKE.asType(type()));
                break;
            case BOUNDED_INVOKE:
                setTarget(BOUNDED_INVOKE.asType(type()));
                break;
            case CONSTRUCT:
                setTarget(CONSTRUCT.asType(type()));
                break;
        }
    }

    @SuppressWarnings("unused")
    public static void setProperty(Object obj, String name, Object val) {
        // TODO: refine
        if (obj instanceof JsObjectObject) {
            $$.cast(obj, JsObjectObject.class).setProperty(name, val);
        } else {
            ReflectionUtil.setFieldValue(obj, name, val);
        }
    }

    @SuppressWarnings("unused")
    public static Object getProperty(Object obj, String name) {
        // TODO: refine
        if (obj instanceof JsObjectObject) {
            return $$.cast(obj, JsObjectObject.class).getProperty(name);
        } else {
            return ReflectionUtil.getFieldValue(obj, name);
        }
    }

    @SuppressWarnings("unused")
    public static Object invoke(Object invoker, Object context, Object... args) {
        // TODO
        Class clazz = invoker.getClass();
        if(ReflectionUtil.isSingleAbstractMethod(clazz)) {
            Method method = invoker.getClass().getMethods()[0];
            return invokeSingleAbstractMethod(invoker, method, args);
        } else if(clazz.getInterfaces().length == 1
            && ReflectionUtil.isSingleAbstractMethod(clazz.getInterfaces()[0])) {
            Method method = clazz.getInterfaces()[0].getMethods()[0];
            return invokeSingleAbstractMethod(invoker, method, args);
        } else if(invoker instanceof JsFunctionObject){
            return $$.cast(invoker, JsFunctionObject.class).invoke(context, args);
        } else {
            return $$.notSupport();
        }
    }

    @SuppressWarnings("unused")
    public static Object boundedInvoke(Object context, Object name, Object... args) {
        if(context instanceof JsObjectObject) {
            Object invoker = getProperty(context, name.toString());
            return invoke(invoker, context, args);
        } else {
            Method method = findExecutable(
                context.getClass().getMethods(), args.length, (String) name);
            try {
                fitArgs(method, args);
                return method.invoke(context, args);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unused")
    public static Object construct(Object invoker, Object...args) {
        if(invoker instanceof JsFunctionObject) {
            return $$.cast(invoker, JsFunctionObject.class).construct(args);
        } else if (invoker instanceof Class) {
            Constructor constructor = findExecutable(
                $$.cast(invoker, Class.class).getConstructors(), args.length, null);

            try {
                fitArgs(constructor, args);
                return constructor.newInstance(args);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            return $$.notSupport();
        }
    }

    private static Object invokeSingleAbstractMethod(Object invoker, Method method, Object... args) {
        try {
            return method.invoke(invoker, args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends Executable> T findExecutable(T[] toMatch, int size, String name) {
        for(T exe: toMatch) {
            if(exe.getParameterCount() == size
                && (name == null || exe.getName().equals(name))) {
                return exe;
            }
        }

        return $$.notSupport();
    }

    private static <T extends Executable> void fitArgs(
        T executable, Object[] args) throws Exception {
        Class[] pTypes = executable.getParameterTypes();

        for(int i = 0;i < args.length;i ++) {
            Object raw = args[i];
            Class targetType = pTypes[i];

            // try to adapt function to lambda callback
            if(JsFunctionObject.class.isAssignableFrom(raw.getClass())
                && ReflectionUtil.isSingleAbstractMethod(targetType)) {
                Method method = targetType.getMethods()[0];
                byte[] content = defineInvokeWrapper(targetType, method);
                // printBytecode(content);

                Class clazz = UNSAFE.defineAnonymousClass(JsIndyCallSite.class, content, null);
                Object instance = clazz.getConstructors()[0].newInstance(raw);
                args[i] = instance;
            }
        }
    }

    private static byte[] defineInvokeWrapper(Class clazz, Method method) {
        String className = "INVOKE_WRAPPER_RECOMPILED";

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(
            Opcodes.V1_8,
            Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER,
            className, null, $$.getInternalName(Object.class),
            new String[] { $$.getInternalName(clazz) });

        //~~~ field
        FieldVisitor fv;
        fv = cw.visitField(Opcodes.ACC_PRIVATE, "function",
            $$.getDescriptor(JsFunctionObject.class), null, null);
        fv.visitEnd();

        MethodVisitor mv;
        //~~~ init method
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(Ljava/lang/Object;)V", null, null);
        mv.visitCode();
        // call super init
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        //~~~ store function
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitTypeInsn(Opcodes.CHECKCAST, $$.getInternalName(JsFunctionObject.class));
        mv.visitFieldInsn(Opcodes.PUTFIELD, className, "function", $$.getDescriptor(JsFunctionObject.class));

        // ret
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        //~~~ invoke method
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(),
            Type.getMethodDescriptor(method), null, null);
        mv.visitCode();
        // get function
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, className, "function", $$.getDescriptor(JsFunctionObject.class));
        // set context to this
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        // new array
        mv.visitIntInsn(Opcodes.BIPUSH, method.getParameterCount());
        mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        // load arguments
        for (int i = 0;i < method.getParameterCount();i ++) {
            mv.visitInsn(Opcodes.DUP);
            mv.visitIntInsn(Opcodes.BIPUSH, i);
            mv.visitVarInsn(Opcodes.ALOAD, i + 1);
            mv.visitInsn(Opcodes.AASTORE);
        }
        // call invoke
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ranttu/rapid/jsvm/runtime/JsFunctionObject",
            "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
        // ret
        if(method.getReturnType() == void.class) {
            mv.visitInsn(Opcodes.POP);
            mv.visitInsn(Opcodes.RETURN);
        } else {
            mv.visitTypeInsn(Opcodes.CHECKCAST, $$.getInternalName(method.getReturnType()));
            mv.visitInsn(Opcodes.ARETURN);
        }
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd();

        return cw.toByteArray();
    }
}