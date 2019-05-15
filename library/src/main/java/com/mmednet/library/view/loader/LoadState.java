package com.mmednet.library.view.loader;

/**
 * Title:LoadState
 * <p>
 * Description:加载状态
 * </p>
 * Author Jming.L
 * Date 2018/7/16 16:42
 */
public enum LoadState {

    UNLOADED("默认的状态", 1),
    LOADING("加载的状态", 2),
    ERROR("失败的状态", 3),
    EMPTY("空的状态", 4),
    SUCCESS("成功的状态", 5);

    private String name;
    private int value;

    LoadState(String name, int value) {
        this.name = name;
        this.value = value;
    }

    int value() {
        return value;
    }
}
