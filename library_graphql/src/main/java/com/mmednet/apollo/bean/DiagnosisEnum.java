package com.mmednet.apollo.bean;

import android.text.TextUtils;

/**
 * Title:ChatDiagnosisEnum
 * <p>
 * Description:问诊类型
 * </p >
 * Author Jming.L
 * Date 2021/2/24 11:00
 */
public enum DiagnosisEnum {

    RICHTEXT("RichText", "图文问诊", "#36b9fe"),
    ONETOONE("OneToOne", "一对一专诊", "#fe693c"),
    FREECLINIC("FreeClinic", "图文问诊", "#36b9fe"),//免费义诊
    INSTANT("Instant", "一对一专诊", "#fe693c");     //急速问诊

    private String type;
    private String content;
    private String color;

    DiagnosisEnum(String type, String content, String color) {
        this.type = type;
        this.content = content;
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getColor() {
        return color;
    }

    public static DiagnosisEnum getEnum(String type) {
        DiagnosisEnum itemEnum = null;
        for (DiagnosisEnum item : DiagnosisEnum.values()) {
            if (TextUtils.equals(item.getType(), type)) {
                itemEnum = item;
                break;
            }
        }
        return itemEnum;
    }

}
