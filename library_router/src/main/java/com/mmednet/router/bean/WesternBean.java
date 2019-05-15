package com.mmednet.router.bean;

import com.mmednet.router.BaseBean;

/**
 * Title:WesternBean
 * <p>
 * Description:向西医诊疗模块
 * </p>
 * Author Jming.L
 * Date 2018/6/1 15:23
 */
public class WesternBean extends BaseBean {

    private static final long serialVersionUID = -5442851870888726392L;
    public String phone;            //患者电话
    public int age;                 //年龄
    public String gender;           //性别
    public String gravid;           //孕否true/false/unknown
    public String lactation;        //哺乳期
    public String married;          //婚否
    public String familyhistory;    //家族史
    public String pasthistory;      //既往史
    public String birthday;         //用户生日
    public String userType;         //用户类型
    public boolean isBindReferral;  //医生转诊时是否绑定公共平台账号

    public String docCountyCode;//县code
    public String docCountyName;//县名称
    public String docTownCode;//镇code
    public String docTownName;//镇名称
    public String docVillageCode;//村code
    public String docVillageName;//村名称

    public boolean isReferral = true;   //是否启用双向转诊功能
    public boolean isUpload = true;     //是否启用药单上传功能

}
