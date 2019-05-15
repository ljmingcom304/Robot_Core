package com.mmednet.reader.bean;

import java.io.Serializable;

/**
 * Title:SimBean
 * <p>
 * Description:SIM卡
 * </p>
 * Author Jming.L
 * Date 2019/3/13 17:07
 */
public class SimBean implements Serializable {

    private static final long serialVersionUID = -5139353254114095676L;

    public String sn;
    public String iccid;

    @Override
    public String toString() {
        return "SimBean{" +
                "sn='" + sn + '\'' +
                ", iccid='" + iccid + '\'' +
                '}';
    }
}
