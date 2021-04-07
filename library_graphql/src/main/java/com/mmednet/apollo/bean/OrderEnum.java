package com.mmednet.apollo.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Title:OrderEnum
 * <p>
 * Description:订单枚举
 * </p >
 * Author Jming.L
 * Date 2021/2/26 11:29
 */
public enum OrderEnum implements Serializable {

    All("", "全部订单"),
    Paying("Paying", "待付款"),
    Pending("Pending", "待服务"),
    Waiting("Waiting", "待接诊"),
    Serving("Serving", "服务中"),
    Started("Started", "已开始"),
    Ending("Ending", "待小结"),
    Cancelled("Cancelled", "已取消"),
    Refunding("Refunding", "退款中"),
    Refunded("Refunding", "已退款"),
    Done("Done", "已完成");

    private String type;
    private String content;

    OrderEnum(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public static OrderEnum getEnum(String type) {
        OrderEnum itemEnum = null;
        for (OrderEnum item : OrderEnum.values()) {
            if (TextUtils.equals(item.getType(), type)) {
                itemEnum = item;
                break;
            }
        }
        return itemEnum;
    }

}