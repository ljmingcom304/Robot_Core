package com.mmednet.router.bean;

import android.app.Activity;

import com.mmednet.router.BaseBean;

/**
 * Title:MiaoBean
 * <p>
 * Description:妙健康
 * </p>
 * Author Jming.L
 * Date 2018/7/6 9:51
 */
public class MiaoBean extends BaseBean {

    public static final int FLAVOR_MIAO = 0;    //妙健康
    public static final int FLAVOR_BELTER = 1;  //倍泰
    public static final int DOCTOR_XINJIANG = 1;//新疆
    private static final long serialVersionUID = 386433434020427441L;
    public String sHIPLoginName;
    public MiaoMonitor monitor;
    public int doctorType;//医生类型0默认1新疆
    public int flavorType;//渠道类型0妙健康1倍泰
    public String name;                         //用户姓名
    public String sex;                          //性别汉字
    public int age;                             //用户年龄
    public String birthday;                     //出生日期
    public Class<?> clazz;                      //Activity
    public boolean isMachine = true;            //是否启用一体机功能

}
