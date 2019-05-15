package com.mmednet.test;

import com.mmednet.library.Library;
import com.mmednet.library.layout.CustomApplication;


public class XApplication extends CustomApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        Library.init(this, "ZXing", true);
    }
}
