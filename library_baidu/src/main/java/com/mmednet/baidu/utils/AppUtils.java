package com.mmednet.baidu.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class AppUtils {

    private static Bundle getMetaData(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new Bundle();
    }

    public static String getAppId(Context context) {
        Bundle metaData = getMetaData(context);
        return String.valueOf(metaData.getInt("com.baidu.speech.APP_ID"));
    }

    public static String getApiKey(Context context){
        Bundle metaData = getMetaData(context);
        return metaData.getString("com.baidu.speech.API_KEY");
    }

    public static String getSecretKey(Context context){
        Bundle metaData = getMetaData(context);
        return metaData.getString("com.baidu.speech.SECRET_KEY");
    }

}
