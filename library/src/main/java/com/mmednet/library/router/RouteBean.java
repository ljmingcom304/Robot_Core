package com.mmednet.library.router;

import java.io.Serializable;

/**
 * Title:RouteBean
 * <p>
 * Description:路由实体
 * </p>
 * Author Jming.L
 * Date 2018/5/25 16:55
 */
public class RouteBean implements Serializable {

    private static final long serialVersionUID = 6684144211528641098L;
    private String host;
    private String path;
    private int priority;
    private String des;
    private String className;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    @Override
    public String toString() {
        return "RouteBean{" +
                "host='" + host + '\'' +
                ", path='" + path + '\'' +
                ", priority=" + priority +
                ", des='" + des + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
