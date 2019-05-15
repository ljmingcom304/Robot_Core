package com.mmednet.library;

import com.mmednet.library.table.Bind;
import com.mmednet.library.table.BindBean;

import java.io.Serializable;

/**
 * Title:PatientBean
 * <p>
 * Description:患者信息（缓存）
 * </p>
 * Author Jming.L
 * Date 2018/5/9 9:45
 */
public class PatientBean implements Serializable {

    private static final long serialVersionUID = 6139851785286908733L;
    @BindBean( {
            @Bind( key = "1", value = "2" ),
            @Bind( key = "1", value = "2" )
    } )
    public int uid;              //经纶用户ID
    @BindBean()
    public String spersonid;        //晶奇用户ID
    public String cardNo;           //身份证号
    public String sNo;              //档案编号
    public String icon = "";             //用户头像
    public String name = "zhangsan";             //用户姓名
    public String phone;            //手机号
    public String contactphone;     //联系人电话
    public String sexName;           //用户性别
    public Float sexCode;          //用户性别编码
    public String birthday;         //用户生日
    public Short age;              //用户年龄
    public Double userType;         //用户类型
    public Byte address;          //用户地址
    public String married;            //是否已婚  是否
    public String pastIllness;        //既往病史
    public String familyIllness;      //家族病史

    public static void getTagName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String name = null;
        int line = 0;
        for (StackTraceElement element : stackTrace) {
            if (PatientBean.class.getName().equals(element.getClassName())) {
                name = element.getClassName();
            } else {
                if (name != null) {
                    name = element.getClassName();
                    line = element.getLineNumber();
                    break;
                }
            }
            System.out.println(element.getClassName());
        }
        StackTraceElement element = stackTrace[1];
        System.out.println("调用=" + name + "[" + line + "]");
    }

    public static void println() {
        getTagName();
    }


}
