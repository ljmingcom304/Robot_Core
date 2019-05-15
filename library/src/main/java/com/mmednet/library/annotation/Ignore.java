package com.mmednet.library.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Title:Ignore
 * <p>
 * Description:
 * </p>
 * Author Jming.L
 * Date 2018/10/22 13:31
 */
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface Ignore {
    boolean ignore() default true;
}
