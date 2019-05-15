package com.mmednet.library.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Title:FontUtils
 * <p>
 * Description:字体工具类
 * </p>
 * Author Jming.L
 * Date 2018/1/30 11:04
 */
public class FontUtils {

    /**
     * 初始化应用字体，需要配置全局样式<item name="android:typeface">monospace</item>
     * @param context   上下文
     * @param fontAssetName 字体文件路径
     */
    public static void initFont(Context context,String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont("MONOSPACE", regular);
    }

    private static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        //android 5.0及以上我们反射修改Typeface.sSystemFontMap变量
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Map<String, Typeface> newMap = new HashMap<>();
            newMap.put(staticTypefaceFieldName, newTypeface);
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField("sSystemFontMap");
                staticField.setAccessible(true);
                staticField.set(null, newMap);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
