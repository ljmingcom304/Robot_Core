package com.mmednet.reader.listener;

import com.mmednet.reader.bean.PersonBean;
import com.mmednet.reader.bean.SimBean;

/**
 * Title:OnReadListener
 * <p>
 * Description:读卡监听
 * </p>
 * Author Jming.L
 * Date 2019/3/14 10:03
 */
public interface OnReadListener {

    void onIdCard(PersonBean bean);

    void onFailure(int code);

    void onSimCard(SimBean bean);

}
