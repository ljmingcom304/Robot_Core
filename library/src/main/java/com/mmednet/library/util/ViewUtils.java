package com.mmednet.library.util;

import android.graphics.Paint;

/**
 * Title:View工具类
 * <p>
 * Description:
 * </p>
 * Author Jming.L
 * Date 2017/8/30 17:01
 */
public class ViewUtils {

    /**
     * 获取字体的实际宽度
     */
    public static float getTextWidth(Paint paint, String text) {
        return text == null ? 0 : paint.measureText(text);
    }

    /**
     * 获取字体的实际高度
     */
    public static float getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 字体垂直居中绘制的偏移量
     */
    public static float getTextOffset(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.abs(Math.ceil(fm.descent + fm.ascent) * 0.5f);
    }
}
