package com.mmednet.library.view.dialog;

import android.content.Context;
import android.view.View;

public interface LayoutDialog{

    void initView(View view);

    void initData();

    int setLayout();

    void dismiss();

    Context getLayoutContext();

    void setOnCloseListener(OnCloseListener listener);

}
