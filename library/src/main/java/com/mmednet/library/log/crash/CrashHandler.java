package com.mmednet.library.log.crash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.mmednet.library.Library;
import com.mmednet.library.common.Constants;
import com.mmednet.library.log.Logger;
import com.mmednet.library.util.FileUtils;
import com.mmednet.library.util.TimeUtils;
import com.mmednet.library.util.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class CrashHandler implements UncaughtExceptionHandler {

    private Context context;
    private String toastMsg;
    private String filePath;
    private boolean isDebug;
    private DateFormat format;
    private Map<String, String> infoMap;                // 用来存储设备信息和异常信息
    private OnCrashHandler onCrashHandler;
    private UncaughtExceptionHandler handler;           //系统默认的UncaughtException处理类

    private static final String EXTENSION = ".log";     //错误报告文件的扩展名
    private static final String TAG = "CrashHandler";

    private CrashHandler() {
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     */
    public static void initCrashHandler(Context context, boolean isDebug) {
        initCrashHandler(context, isDebug, null);
    }

    private static void initCrashHandler(Context context, boolean isDebug, String toastMsg) {
        initCrashHandler(context, isDebug, toastMsg, null);
    }

    private static void initCrashHandler(Context context, boolean isDebug, String toastMsg,
                                         OnCrashHandler onCrashHandler) {
        final CrashHandler crashHandler = new CrashHandler();
        if (null != toastMsg) {
            crashHandler.toastMsg = toastMsg;
        } else {
            crashHandler.toastMsg = "Crash";
        }

        crashHandler.isDebug = isDebug;
        crashHandler.infoMap = new TreeMap<>();
        crashHandler.onCrashHandler = onCrashHandler;
        crashHandler.context = Library.getInstance().getContext();
        crashHandler.filePath = FileUtils.getCachePath(context, Constants.CRASH);
        crashHandler.format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault());
        if (isDebug) {
            crashHandler.handler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
        } else {
            crashHandler.handler = new UncaughtCrashHandler(crashHandler.context);//保证应用不会崩溃
        }
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @SuppressWarnings( "unused" )
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (handler != null) {
            if (isDebug) {
                handleException(ex);
            }
            handler.uncaughtException(thread, ex);
        }
    }

    //处理异常信息返回true，未处理返回false
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        if (onCrashHandler != null && onCrashHandler.onCrash(ex)) {
            return true;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                // Toast 显示需要出现在一个线程的消息队列中
                Looper.prepare();
                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        //收集设备参数信息
        collectDeviceInfo();
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     */
    private void collectDeviceInfo() {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infoMap.put("VersionName", versionName);
                infoMap.put("VersionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infoMap.put(field.getName(), field.get(null).toString());
                Logger.e(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Logger.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存日志文件
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        String time = TimeUtils.millisToStringDate(System
                .currentTimeMillis()) + System.getProperty("line.separator");
        sb.append(time);

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        //设备信息
        for (Map.Entry<String, String> entry : infoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String device = key + "=" + value + System.getProperty("line.separator");
            sb.append(device);
        }
        Logger.e(TAG, result);

        OutputStream os = null;
        String fileName = format.format(new Date()) + "-" + System.currentTimeMillis()
                + EXTENSION;
        File file = new File(filePath + fileName);
        try {
            if (!file.exists() && !file.createNewFile()) {
                Logger.e(TAG, "CrashHandler处理文件创建失败");
                return null;
            }

            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                os = new FileOutputStream(file);
                os.write(sb.toString().getBytes());
                os.close();
            }
            return fileName;
        } catch (Exception e) {
            Logger.e(TAG, "an error occured while writing file...", e);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
