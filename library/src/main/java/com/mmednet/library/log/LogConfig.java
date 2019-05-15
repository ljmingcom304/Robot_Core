package com.mmednet.library.log;

import android.content.Context;

import com.mmednet.library.common.Constants;
import com.mmednet.library.util.FileUtils;

/**
 * Title:LogConfig
 * <p>
 * Description:日志配置
 * </p>
 * Author Jming.L
 * Date 2017/8/29 12:01
 */
public class LogConfig {

    static final String LOG_REPORTER_EXTENSION = ".log";
    private static LogConfig config;

    private boolean debug;              // 开启控制台输出模式
    private boolean logFile;            // 开启客户端本地日志记录模式
    private String logFilePath;         // 本地日志记录的路径

    private LogConfig() {
        debug = true;
        logFile = false;
    }

    static synchronized LogConfig getInstance() {
        if (config == null) {
            config = new LogConfig();
        }
        return config;
    }

    /**
     * @param debug   开启控制台输出模式
     * @param logFile 开启客户端本地日志记录模式
     */
    public static void initLogger(Context context, boolean debug, boolean logFile) {
        initLogger(debug, logFile, FileUtils.getCachePath(context, Constants.INFO));
    }

    private static void initLogger(boolean debug, boolean logFile, String logFilePath) {
        ThreadPool.initThreadPool(3);// 初始化线程池
        LogConfig instance = LogConfig.getInstance();
        instance.debug = debug;
        instance.logFile = logFile;
        instance.logFilePath = logFilePath;
    }

    boolean isDebug() {
        return debug;
    }

    boolean isLogFile() {
        return logFile;
    }

    String getLogFilePath() {
        return logFilePath;
    }

}
