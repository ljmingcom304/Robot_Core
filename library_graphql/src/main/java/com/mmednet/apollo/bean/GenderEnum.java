package com.mmednet.apollo.bean;

import android.text.TextUtils;

import java.io.Serializable;

public enum GenderEnum implements Serializable {

    MALE("Male","男"),
    FEMALE("Female","女");

    private String type;
    private String content;

    GenderEnum(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public static GenderEnum getEnum(String type) {
        GenderEnum itemEnum = null;
        for (GenderEnum item : GenderEnum.values()) {
            if (TextUtils.equals(item.getType(), type)) {
                itemEnum = item;
                break;
            }
        }
        return itemEnum;
    }

}
