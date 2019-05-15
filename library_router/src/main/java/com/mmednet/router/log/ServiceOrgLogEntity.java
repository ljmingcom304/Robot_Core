package com.mmednet.router.log;

import java.io.Serializable;

/**
 * 功能说明：记录日志时需要服务者所属机构需要记录的信息
 *
 * @author xiaoyu
 * @date 2018/5/25
 */
public class ServiceOrgLogEntity  implements Serializable {

    private static final long serialVersionUID = 8204026325352969251L;
    /**
     * 机构名称
     */
    public String orgName;
    /**
     * 机构编码
     */
    public String orgCode;

    /**
     * 区域编码
     */
    public String areaCode;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
