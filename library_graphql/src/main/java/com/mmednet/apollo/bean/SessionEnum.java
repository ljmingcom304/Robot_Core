package com.mmednet.apollo.bean;

import android.text.TextUtils;

/**
 * Title:ChatSessionEnum
 * <p>
 * Description:服务状态
 * </p >
 * Author Jming.L
 * Date 2021/2/24 11:11
 */
public enum SessionEnum {

    ACTIVE("Active", "服务中"),
    DISABLED("Disabled", "待服务");

    private String active;
    private String content;

    SessionEnum(String active, String content) {
        this.active = active;
        this.content = content;
    }

    public String getActive() {
        return active;
    }

    public String getContent() {
        return content;
    }

    public static SessionEnum getEnum(String type) {
        SessionEnum itemEnum = null;
        for (SessionEnum item : SessionEnum.values()) {
            if (TextUtils.equals(item.getActive(), type)) {
                itemEnum = item;
                break;
            }
        }
        return itemEnum;
    }

}
