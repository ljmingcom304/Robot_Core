package com.mmednet.library.view.edit;

import android.graphics.drawable.Drawable;

import java.util.List;

public interface EditView {

    /**
     * 是否可以编辑
     *
     * @return true or false
     */
    boolean isEditable();

    /**
     * 设置可否编辑
     *
     * @param editable true or false
     */
    void setEditable(boolean editable);

    /**
     * 设置内容字体大小
     *
     * @param size 字体尺寸
     */
    void setTxtSize(int size);

    /**
     * 设置提示的颜色
     *
     * @param color 颜色
     */
    void setHintColor(int color);

    /**
     * 设置内容字体颜色
     *
     * @param color 颜色
     */
    void setTxtColor(int color);

    /**
     * 添加提示内容
     *
     * @param hint 提示内容
     */
    void addHint(String... hint);

    /**
     * 设置提示内容
     *
     * @param hint 提示内容
     */
    void setHint(String... hint);

    /**
     * 获取提示内容
     *
     * @return 提示内容集合
     */
    List<String> getHints();

    /**
     * 设置显示内容
     *
     * @param text 显示内容
     */
    void setTexts(String... text);

    /**
     * 获得显示内容
     *
     * @return 显示内容
     */
    List<String> getTexts();

    /**
     * 设置控件的局部背景样式框
     */
    void setBackgroundView(int resId);

    /**
     * 设置编辑框文本图形
     *
     * @param left 左侧图形文本
     */
    void setLeftDrawable(String left);


    /**
     * 设置编辑框右侧图形
     *
     * @param left 左侧图形
     */
    void setLeftDrawable(Drawable left);

    /**
     * 设置编辑框文本图形
     *
     * @param right 右侧图形文本
     */
    void setRightDrawable(String right);

    /**
     * 设置编辑框右侧图形
     *
     * @param right 右侧图形
     */
    void setRightDrawable(Drawable right);

    /**
     * 设置编辑框输入类型
     *
     * @param type 输入类型
     */
    void setEditInputType(int type);

    /**
     * 点击监听
     *
     * @param listener 监听器
     */
    void setOnItemEditListener(OnItemEditListener listener);

}
