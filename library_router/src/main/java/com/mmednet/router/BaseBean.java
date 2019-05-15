package com.mmednet.router;

import java.io.Serializable;

public class BaseBean implements Serializable {

    private static final long serialVersionUID = -2129763494956907976L;
    public String addressIP;        //主机地址
    public String doctorId;            //医生ID
    public String doctorPhone;      //医生手机号
    public String doctorName;       //医生姓名
    public String sOrganizationKey; //医生机构主键
    public String sOrganizationName;//医生机构名称
    public String sOrgInstitutionCode; //医生机构编码
    public String sManagementDivisionCode;//区域编码
    public String sManagementDivisionName;//区域名称

    public String patientId;           //用户ID
    public String spersonname;      //患者姓名
    public String spersonid;        //用户晶奇标识
    public String patientCard;      //患者身份证号

}
