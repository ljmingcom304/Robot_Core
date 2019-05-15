package com.mmednet.router.bean;

import com.mmednet.router.BaseBean;

/**
 * Title:TcmBean
 * <p>
 * Description:向中医诊疗模块
 * </p>
 * Author Jming.L
 * Date 2018/6/12 16:54
 */
public class TcmBean extends BaseBean {

    private static final long serialVersionUID = -7961906020303803067L;
    public String phone;            //患者电话
    public int age;                 //年龄
    public String gender;           //性别
    public String birthday;         //用户生日
    public String userType;         //用户类型

    public String docCountyCode;//县code
    public String docCountyName;//县名称
    public String docTownCode;//镇code
    public String docTownName;//镇名称
    public String docVillageCode;//村code
    public String docVillageName;//村名称

    public boolean isUpload = true; //是否启用药单上传功能

}
