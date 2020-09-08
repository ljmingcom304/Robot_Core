package com.mmednet.library.robot.engine.operation;


import androidx.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    @StringRes int[] actionId() default {0};
    String[] actionTexts() default {"${mmednet}"};
}
