package com.mmednet.library.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface BindView {
    boolean toView() default true;//是否支持语音赋值;

    String[] formats() default {"yyyy年MM月dd日","yyyy年MM月dd号"};//日期格式化

    String result() default "yyyy-MM-dd";//目标格式
}
