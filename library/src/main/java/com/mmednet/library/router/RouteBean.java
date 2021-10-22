package com.mmednet.library.router;

import java.io.Serializable;
import java.util.Objects;

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
                ", des='" + des + '\'' +
                ", className='" + className + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteBean routeBean = (RouteBean) o;
        return Objects.equals(host, routeBean.host) &&
                Objects.equals(path, routeBean.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, path);
    }
}
