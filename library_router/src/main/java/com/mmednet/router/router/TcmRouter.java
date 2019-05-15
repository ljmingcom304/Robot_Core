package com.mmednet.router.router;

import com.mmednet.router.BaseRouter;
import com.mmednet.router.bean.WesternBean;

/**
 * Title:TcmRouter
 * <p>
 * Description:中医诊疗模块
 * </p>
 * Author Jming.L
 * Date 2018/6/12 16:47
 */
public class TcmRouter extends BaseRouter {

    /**
     * 实体类
     */
    public static final String BEAN = TcmRouter.class.getSimpleName();

    /**
     * 主机名
     */
    public static final String HOST = "com.mmednet.tcm";

    /**
     * 中医诊疗入口页
     */
    public static final String PATH_TREAT = "PATH_TREAT";

}
