package com.mmednet.klyl.callback;

import com.mmednet.klyl.KLCallback;

public class OnListener {

    private int type;
    private String tag;
    private KLCallback callback;

    public OnListener(KLCallback callback, String tag, int type) {
        this.tag = tag;
        this.type = type;
        this.callback = callback;
    }

    public void onListen(String message, int msgType) {
        if (callback != null) {
            if (msgType == type) {
                callback.onBack(message);
            }
        }
    }

    public String getTag() {
        return tag;
    }
}
