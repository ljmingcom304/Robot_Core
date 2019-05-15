package com.mmednet.library.util;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.mmednet.library.Library;
import com.mmednet.library.log.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import static android.R.attr.versionName;

public class AppUtils {

    private static final String TAG = "AppUtils";

    /**
     * 是否为主进程
     */
    public static boolean isMainProgress(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        return TextUtils.equals(packageName, getProcessName(context));
    }

    /**
     * 获取当前进程名
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 打印屏幕密度
     */
    public static void printDensity() {
        Context instance = Library.getInstance().getContext();
        DisplayMetrics displayMetrics = instance.getResources()
                .getDisplayMetrics();
        Logger.e(TAG, "屏幕密度： " + displayMetrics.density + " 屏幕密度DPI："
                + displayMetrics.densityDpi + " height: "
                + displayMetrics.heightPixels + " width: "
                + displayMetrics.widthPixels);
    }

    /**
     * 打印屏幕尺寸
     */
    public static void printScreenSize() {
        Point point = new Point();
        Context instance = Library.getInstance().getContext();
        WindowManager manager = (WindowManager) instance
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = instance.getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Logger.d(TAG, "Screen inches : " + screenInches);
    }

    /**
     * 获取设备IMEI编号
     */
    public static String getIMEI() {
        Context instance = Library.getInstance().getContext();
        TelephonyManager telephonyManager = (TelephonyManager) instance.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 设备首次启动时生成的ID
     */
    public static String getSystemID() {
        Context instance = Library.getInstance().getContext();
        @SuppressWarnings( "deprecation" )
        String id = Settings.System.getString(instance
                .getContentResolver(), Settings.System.ANDROID_ID);
        return id;
    }

    /**
     * 获取设备序列号
     */
    public static String getDeviceId() {
        Context instance = Library.getInstance().getContext();
        TelephonyManager telephonyManager = (TelephonyManager) instance
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimSerialNumber();
    }

    /**
     * 获取屏幕亮度0-255
     */
    public static int getScreenBrightness() {
        Context instance = Library.getInstance().getContext();
        ContentResolver contentResolver = instance.getContentResolver();
        return Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    /**
     * 设置当前屏幕亮度
     */
    public static void setScreenBrightness(int value) {
        setScreenManualMode();
        Context instance = Library.getInstance().getContext();
        ContentResolver contentResolver = instance.getContentResolver();
        value = Math.min(255, value);
        value = Math.max(0, value);
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, value);
    }

    /**
     * 亮度设置为手动调节模式
     */
    private static void setScreenManualMode() {
        Context instance = Library.getInstance().getContext();
        ContentResolver contentResolver = instance.getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置系统当前音量
     */
    public static void setStreamVolume(int value) {
        int volume = Math.max(0, value);
        volume = Math.min(getStreamMaxVolume(), volume);
        Context instance = Library.getInstance().getContext();
        AudioManager mAudioManager = (AudioManager) instance
                .getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    /**
     * 获取系统当前音量
     */
    public static int getStreamVolume() {
        Context instance = Library.getInstance().getContext();
        AudioManager mAudioManager = (AudioManager) instance
                .getSystemService(Context.AUDIO_SERVICE);
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 获取系统最大音量
     */
    public static int getStreamMaxVolume() {
        Context instance = Library.getInstance().getContext();
        AudioManager mAudioManager = (AudioManager) instance
                .getSystemService(Context.AUDIO_SERVICE);
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 获取系统可读写的总空间
     */
    public static String getSysTotalSize() {
        float size = getTotalSize("/data") / (float) Math.pow(1024, 3);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(size) + " G";
    }

    /**
     * 计算系统的剩余空间
     */
    public static String getSysAvailableSize() {
        float size = getAvailableSize("/data") / (float) Math.pow(1024, 3);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(size) + " G";
    }

    /**
     * 计算剩余空间
     */
    @SuppressWarnings( "deprecation" )
    private static long getAvailableSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return (long) fileStats.getAvailableBlocks() * fileStats.getBlockSize(); // 注意与fileStats.getFreeBlocks()的区别
    }

    /**
     * 计算总空间
     */
    @SuppressWarnings( "deprecation" )
    private static long getTotalSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return (long) fileStats.getBlockCount() * fileStats.getBlockSize();
    }

    /**
     * 获取MetaData数据
     */
    public static String getMetaDate(String name) {
        Context instance = Library.getInstance().getContext();
        PackageManager packageManager = instance.getPackageManager();
        String packageName = instance.getPackageName();
        ApplicationInfo appInfo = null;
        try {
            appInfo = packageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo == null || appInfo.metaData == null) {
            return null;
        }
        return appInfo.metaData.getString(name);
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName() {
        String versionName = "";
        Context instance = Library.getInstance().getContext();
        PackageManager pm = instance.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(instance.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (versionName == null || versionName.length() <= 0) {
            return "";
        }
        return versionName;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode() {
        int versionCode = 0;
        Context instance = Library.getInstance().getContext();
        PackageManager pm = instance.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(instance.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取包名
     */
    public static String getPackageName() {
        Context instance = Library.getInstance().getContext();
        return instance.getPackageName();
    }

}
