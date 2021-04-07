package com.mmednet.apollo.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Title:DoctorEnum
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2021/2/26 15:36
 */
public enum DoctorEnum implements Serializable {

    DEFAULT("Default","医生"),EXPERT("Expert","专家"),GURU("Guru","名医");
    private static final long serialVersionUID = -5899752263515734237L;
    private String type;
    private String content;

    DoctorEnum(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

}
