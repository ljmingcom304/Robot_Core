package com.mmednet.ocr.utils;

import android.content.Context;

import java.io.File;

/**
 * Title:FileUtils
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2021/1/22 14:23
 */
public class FileUtils {

    public static File getSaveFile(Context context) {
        return new File(context.getFilesDir(), "orc.jpg");
    }

}
