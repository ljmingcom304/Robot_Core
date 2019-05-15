package com.mmednet.library.router;

import android.content.Intent;

/**
 * Title:RouteIntent
 * <p>
 * Description:定义目标主机与目标路径，符合该策略的结点会被抓取
 * </p>
 * Author Jming.L
 * Date 2018/5/29 14:24
 */
public class RouteIntent extends Intent {

    private String host;
    private String path;

    public RouteIntent(String path) {
        this.path = path;
    }

    public RouteIntent(String host, String path) {
        this.host = host;
        this.path = path;
    }

    /**
     * 目标主机
     */
    public String host() {
        return host;
    }

    /**
     * 目标路径
     */
    public String path() {
        return path;
    }

}
