package ranttu.rapid.jsvm.jscomp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this name should export
 *
 * @author rapidhere@gmail.com
 * @version $id: ExportName.java, v0.1 2017/4/19 dongwei.dq Exp $
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportName {
}
