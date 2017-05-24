package ranttu.rapid.jsvm.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Property Manager
 *
 * @author rapidhere@gmail.com
 * @version $id: JsIndyFactory.java, v0.1 2017/4/15 dongwei.dq Exp $
 */
final public class SystemProperty {
    @Property(namespace = "jsvm")
    public static boolean UseOptimisticCallSite = true;

    @Property(namespace = "jsvm")
    public static boolean UseVAC = true;

    @Property(namespace = "jsvm.test")
    public static boolean PrintByteCode = false;

    @Property(namespace = "jsvm.test")
    public static boolean IgnoreBenchMark = false;

    //~~~ implementations
    // private constructor
    private SystemProperty() {}

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface Property {
        String namespace();
    }

    private static void initProperties() throws Throwable {
        Class<SystemProperty> clazz = SystemProperty.class;

        for (Field field: clazz.getDeclaredFields()) {
            if(field.isAnnotationPresent(Property.class)) {
                Property property = field.getAnnotation(Property.class);

                String name = property.namespace() + "." +
                    resolvePropertyName(field);
                String val = System.getProperty(name);

                // ignore and use default value
                if(val == null) {
                    continue;
                }

                if (field.getType() == boolean.class) {
                    field.setBoolean(null, Boolean.valueOf(val));
                } else {
                    field.set(null, val);
                }
            }
        }
    }

    private static String resolvePropertyName(Field field) {
        String rawName = field.getName();
        String[] names = rawName.split("_");
        StringBuilder ret = new StringBuilder();
        for(String name: names) {
            ret.append(name.substring(0, 1).toLowerCase())
                .append(name.substring(1)).append(".");
        }

        return ret.substring(0, ret.length() - 1);
    }

    static {
        try {
            initProperties();
        } catch (Throwable e) {
            System.err.print("init failed");
            e.printStackTrace();
        }
    }
}
