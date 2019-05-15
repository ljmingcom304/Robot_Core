package com.mmednet.router.bean;

import java.io.Serializable;

/**
 * Title:MiaoMonitor
 * <p>
 * Description:监测类型
 * </p>
 * Author Jming.L
 * Date 2018/7/6 10:00
 */
public enum MiaoMonitor implements Serializable {

    //  10047 手环
    //  10048 手表
    //  10049 血糖仪
    //  10050 血压计
    //  10051 血氧机
    //  10052 体温计
    //  10053 体脂秤
    //  10054 老人类
    //  10055 睡眠监测仪
    //  10056 智能鞋服
    //  10050 血压
    //  10049 血糖
    //  10051 血氧
    //  10053 体重
    //  10052 体温

    XY("血压", 100, 3, 10050), XT("血糖", 101, 4, 10049), XO("血氧", 102, 9, 10051),
    XL("心率", 104, 1, -1), TW("体温", 106, 5, 10052), TZ("体重", 108, 7, 10053),
    XZ("血脂", 103, -1, -1), XD("心电", 105, -1, -1), NS("尿酸尿检", 107, -1, -1);

    private String name;        //监测名称
    private int robotId;        //连接全科医生助手ID
    private int miaoId;         //连接妙健康ID
    private long deciveId;       //区分不同监测类型的妙健康ID

    MiaoMonitor(String name, int robotId, int miaoId, long deciveId) {
        this.name = name;
        this.robotId = robotId;
        this.miaoId = miaoId;
        this.deciveId = deciveId;
    }

    public String getName() {
        return name;
    }

    public int getRobotId() {
        return robotId;
    }

    public int getMiaoId() {
        return miaoId;
    }

    public long getDeciveId() {
        return deciveId;
    }
}
