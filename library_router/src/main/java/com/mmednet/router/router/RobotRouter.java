package com.mmednet.router.router;

import com.mmednet.router.BaseRouter;
import com.mmednet.router.bean.RobotBean;
import com.mmednet.router.bean.WesternBean;

/**
 * Title:RobotRouter
 * <p>
 * Description:主体模块
 * </p>
 * Author Jming.L
 * Date 2018/6/1 14:17
 */
public class RobotRouter extends BaseRouter {

    /**
     * 实体类
     */
    public static final String BEAN = RobotBean.class.getSimpleName();

    /**
     * 主机名
     */
    public static final String HOST = "com.mmednet.robot";

    /**
     * 双向转诊
     */
    public static final String PATH_REFERRAL = "PATH_REFERRAL";

    /**
     * 双向转诊绑定
     */
    public static final String PATH_REFERRAL_BIND = "PATH_REFERRAL_BIND";

    /**
     * 患者资料
     */
    public static final String PATH_PATIENT_DATA = "PATH_PATIENT_DATA";

    /**
     * 监测页面
     */
    public static final String PATH_MONITOR = "PATH_MONITOR";

}
