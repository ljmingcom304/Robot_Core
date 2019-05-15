package com.mmednet.router.log;


import java.io.Serializable;

/**
 * Title:BaseLogEntity
 * <p>
 * Description:日志
 * </p>
 * Author Jming.L
 * Date 2018/6/7 18:39
 */
public class BaseLogEntity implements Serializable {

    private static final long serialVersionUID = -3744323122738195176L;
    public ServiceConsumerLogEntity consumerLogEntity;

    public ServiceProviderLogEntity providerLogEntity;

    public ServiceOrgLogEntity serviceOrgLogEntity;

}
