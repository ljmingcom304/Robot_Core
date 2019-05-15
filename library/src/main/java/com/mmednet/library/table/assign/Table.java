package com.mmednet.library.table.assign;

import com.mmednet.library.table.BindTable;

import java.util.List;

/**
 * Title:Table
 * <p>
 * Description:实现该接口的可以进行动态注入
 * </p>
 * Author Jming.L
 * Date 2018/10/31 14:29
 */
public interface Table {

    /**
     * 设置内容
     *
     * @param texts 内容
     */
    void setText(List<String> texts);

    /**
     * 获取内容
     *
     * @return 内容
     */
    List<String> getTexts();

    /**
     * 获取显示状态，显示状态下支持动态注入，非显示状态下不支持动态注入及语音赋值
     *
     * @return 显示状态
     */
    int getVisibility();

}
