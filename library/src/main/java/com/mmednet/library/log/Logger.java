package com.mmednet.library.log;

import android.os.Environment;
import android.util.Log;

import com.mmednet.library.util.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Title:Logger
 * <p>
 * Description:日志工具类
 * </p>
 * Author Jming.L
 * Date 2017/8/29 12:01
 */
public class Logger {

    private static final String TAG = "Logger";
    private static final String VERBOSE = "VERBOSE";
    private static final String DEBUG = "DEBUG";
    private static final String INFO = "INFO";
    private static final String WARN = "WARN";
    private static final String ERROR = "STATE_ERROR";

    private static String className;
    private static int lineNum;

    public static void v(String msg) {
        log(getTagName(), msg, null, VERBOSE);
    }

    public static void v(String tag, String msg) {
        log(tag, msg, null, VERBOSE);
    }

    public static void v(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, VERBOSE);
    }

    public static void d(String msg) {
        log(getTagName(), msg, null, DEBUG);
    }

    public static void d(String tag, String msg) {
        log(tag, msg, null, DEBUG);
    }

    public static void d(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, DEBUG);
    }

    public static void i(String msg) {
        log(getTagName(), msg, null, INFO);
    }

    public static void i(String tag, String msg) {
        log(tag, msg, null, INFO);
    }

    public static void i(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, INFO);
    }

    public static void w(String msg) {
        log(getTagName(), msg, null, WARN);
    }

    public static void w(String tag, String msg) {
        log(tag, msg, null, WARN);
    }

    public static void w(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, WARN);
    }

    public static void w(String tag, Throwable tr) {
        log(tag, null, tr, WARN);
    }

    public static void e(String msg) {
        log(getTagName(), msg, null, ERROR);
    }

    public static void e(String tag, String msg) {
        log(tag, msg, null, ERROR);
    }

    public static void e(String tag, String msg, Throwable tr) {
        log(tag, msg, tr, ERROR);
    }

    public static void e(String tag, Throwable tr) {
        log(tag, "", tr, ERROR);
    }

    /**
     * 获取调用的类名
     */
    private static String getTagName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String name = null;
        int line = 0;
        if (stackTrace != null) {
            for (StackTraceElement element : stackTrace) {
                if (Logger.class.getName().equals(element.getClassName())) {
                    name = element.getClassName();
                } else {
                    if (name != null) {
                        name = element.getClassName();
                        line = element.getLineNumber();
                        break;
                    }
                }
            }
        }
        return name + "[" + line + "]";
    }

    private static void log(String tag, String msg, Throwable tr, String priority) {
        LogConfig config = LogConfig.getInstance();
        if (config.isDebug()) {
            switch (priority) {
                case VERBOSE:
                    android.util.Log.v(tag, msg, tr);
                    break;
                case DEBUG:
                    android.util.Log.d(tag, msg, tr);
                    break;
                case INFO:
                    android.util.Log.i(tag, msg, tr);
                    break;
                case WARN:
                    android.util.Log.w(tag, msg, tr);
                    break;
                case ERROR:
                    android.util.Log.e(tag, msg, tr);
                    break;
                default:
                    break;
            }
        }
        if (config.isLogFile()) {
            initStackTraceInfo();
            writeLog(tag, msg, tr, priority);
        }
    }

    private static void initStackTraceInfo() {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        int num = 3;
        if (stacks != null && stacks.length >= num) {
            lineNum = stacks[num].getLineNumber();
            className = stacks[num].getFileName();
            int index = className.lastIndexOf(".");
            if (index > 0) {
                className = className.substring(0, index);
            }
        }
    }

    private static void writeLog(String tag, String msg, Throwable tr,
                                 String priority) {
        LogConfig config = LogConfig.getInstance();
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) || config.getLogFilePath() == null) {
            return;
        }
        ThreadPool.execute(new RunTask<Void, Void>(tag, msg, tr, priority) {
            @Override
            public Void runInBackground() {
                synchronized (Logger.class) {
                    String tag = (String) objs[0];
                    String msg = (String) objs[1];
                    Throwable tr = (Throwable) objs[2];
                    String priority = (String) objs[3];
                    String logFilePath = LogConfig.getInstance().getLogFilePath();
                    if (!logFilePath.endsWith(File.separator)) {
                        logFilePath = logFilePath + File.separator;
                    }

                    File file = new File(logFilePath + className + LogConfig.LOG_REPORTER_EXTENSION);

                    OutputStream os = null;
                    try {
                        if (!file.exists() && !file.createNewFile()) {
                            Logger.e(TAG, "Logger creat fail");
                            return null;
                        }

                        boolean time = Math.abs(file.lastModified() - System.currentTimeMillis()) > 1000 * 60 * 60 * 6;
                        os = new FileOutputStream(file, !time);

                        String separator = System.getProperty("line.separator");
                        String formatMsg = TimeUtils.millisToStringDate(System
                                .currentTimeMillis()) + separator
                                + "[Level=" + priority + "][ClassName=" + className + "][LineNumber=" + lineNum + "][Tag=" + tag + "]:"
                                + separator
                                + "User Message: " + msg
                                + separator
                                + (null == tr ? "" :
                                "Throwable Message: "
                                        + tr.getMessage()
                                        + separator
                                        + "Throwable StackTrace: "
                                        + transformStackTrace(tr
                                        .getStackTrace()))
                                + separator;

                        os.write(formatMsg.getBytes());
                        os.flush();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        if (null != os) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return null;
            }
        });

    }

    private static StringBuilder transformStackTrace(StackTraceElement[] elements) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : elements) {
            sb.append(element.toString()).append("\r\n");
        }
        return sb;
    }

}
