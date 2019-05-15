package com.mmednet.router.log;

import java.io.Serializable;

/**
 * 功能说明：被服务对象 记录日志时需要信息
 *
 * @author xiaoyu
 * @date 2018/5/25
 */
public class ServiceConsumerLogEntity  implements Serializable {

    private static final long serialVersionUID = -7602606452917210541L;
    /**
     * 服务消费用户唯一标识
     */
    public String id;

    /**
     * 服务消费用户名称
     */
    public String name;

    /**
     * 服务消费者身份证号
     */
    public String idcard;

    /**
     * 性别 男/女
     */
    public String sex;

    /**
     * 年龄
     */
    public int age;

    /**
     * 服务消费者TAG
     */
    public String tag;
    /**
     * 用户住址
     */
    public String address;
    /**
     * 用户手机号
     */
    public String phone;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
