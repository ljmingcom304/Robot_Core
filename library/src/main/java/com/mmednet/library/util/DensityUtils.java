package com.mmednet.library.util;

import android.content.Context;
import android.util.TypedValue;

import com.mmednet.library.Library;


/**
 * Title:DensityUtils
 * <p>
 * Description:单位转换
 * </p>
 * Author Jming.L
 * Date 2017/8/30 16:58
 */
public class DensityUtils {

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources()
                        .getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources()
                        .getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources()
                .getDisplayMetrics().density;
        return (pxVal / scale + 0.5f);
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources()
                .getDisplayMetrics().scaledDensity);
    }

    /**
     * dp转px
     */
    @Deprecated
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, Library.getInstance().getContext().getResources()
                        .getDisplayMetrics());
    }

    /**
     * sp转px
     */
    @Deprecated
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, Library.getInstance().getContext().getResources()
                        .getDisplayMetrics());
    }

    /**
     * px转dp
     */
    @Deprecated
    public static float px2dp(float pxVal) {
        final float scale = Library.getInstance().getContext().getResources()
                .getDisplayMetrics().density;
        return (pxVal / scale + 0.5f);
    }

    /**
     * px转sp
     */
    @Deprecated
    public static float px2sp(float pxVal) {
        return (pxVal / Library.getInstance().getContext().getResources()
                .getDisplayMetrics().scaledDensity);
    }

}
