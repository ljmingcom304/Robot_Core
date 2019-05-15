package com.mmednet.router.log;

import java.io.Serializable;

/**
 * 功能说明：记录日志时需要服务者需要记录的信息
 *
 * @author xiaoyu
 * @date 2018/5/25
 */
public class ServiceProviderLogEntity  implements Serializable {

    private static final long serialVersionUID = -1346938725332242127L;
    /**
     * 服务者用户唯一标识
     */
    public String id;

    /**
     * 服务者用户名称
     */
    public String name;

    /**
     * 医生别名
     */
    public String alias;

    /**
     * 医生联系方式
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
