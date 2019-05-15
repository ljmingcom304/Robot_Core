package com.mmednet.reader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Title:Thread
 * <p>
 * Description:方法是否需要在子线程执行
 * </p>
 * Author Jming.L
 * Date 2019/3/13 17:52
 */
@Target( value = ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@interface Thread {

}
