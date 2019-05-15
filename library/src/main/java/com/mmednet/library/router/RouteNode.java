package com.mmednet.library.router;

import com.mmednet.library.BuildConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RouteNode{

    /**
     * host of one route
     */
    String host() default "";//主机

    /**
     * path of one route
     */
    String path();        //路径

    /**
     * The priority of route.
     */
    int priority() default -1;    //优先级

    /**
     * description of the activity, user for gen route table
     */
    String desc() default "";    //描述



}
