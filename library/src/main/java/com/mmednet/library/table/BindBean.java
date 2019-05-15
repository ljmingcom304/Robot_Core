package com.mmednet.library.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface BindBean {
    // 字典>资源ID>资源
    String dict() default ""; // 字典

    Bind[] value() default {};//

    /**
     * 已废弃
     */
    String code() default ""; // 编码：男=1|女=2|未知性别=3

    /**
     * 已废弃
     */
    @Deprecated
    String index() default "";// 索引：男|女|未知性别 编码：0|1|2

}
