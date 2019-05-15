package com.mmednet.library.robot.engine.operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Title:Explain
 * <p>
 * Description:意图说明
 * </p>
 * Author Jming.L
 * Date 2017/8/15 13:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Explain {
    String description() default "";
}
