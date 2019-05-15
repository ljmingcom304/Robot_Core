package com.mmednet.router.router;

import com.mmednet.router.BaseRouter;
import com.mmednet.router.bean.WesternBean;

/**
 * Title:WesternRouter
 * <p>
 * Description:西医诊疗模块
 * </p>
 * Author Jming.L
 * Date 2018/6/1 14:18
 */
public class WesternRouter extends BaseRouter {

    /**
     * 实体类
     */
    public static final String BEAN = WesternBean.class.getSimpleName();

    /**
     * 主机名
     */
    public static final String HOST = "com.mmednet.western";

    /**
     * 模块选择页
     */
    public static final String PATH_SELECT = "PATH_SELECT";


}
