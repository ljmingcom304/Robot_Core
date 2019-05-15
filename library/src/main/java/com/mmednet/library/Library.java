package com.mmednet.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mmednet.library.analyze.Analyzer;
import com.mmednet.library.database.helper.DatabaseHelper;
import com.mmednet.library.log.LogConfig;
import com.mmednet.library.log.crash.CrashHandler;
import com.mmednet.library.robot.Robot;
import com.mmednet.library.robot.engine.WholeVoice;
import com.mmednet.library.robot.manage.Manager;
import com.mmednet.library.router.Route;

import java.util.List;

/**
 * Title:Library
 * <p>
 * Description:库工程
 * </p>
 * Author Jming.L
 * Date 2017/9/4 13:59
 */
public class Library {

    private Context context;                    //上下文
    private String rootDir;                     //根目录
    private String buildType;                   //构建类型
    private boolean isDebug;                    //测试类型
    private int databaseVersion;                //数据库版本
    private List<Class<?>> databaseBeans;       //数据库Bean

    private Library() {
    }

    public static Library getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private Library library;

        Singleton() {
            library = new Library();
        }

        public Library getInstance() {
            return library;
        }
    }

    /**
     * 初始化资源
     *
     * @param context 上下文
     * @param rootDir Sdcard根目录
     * @param isDebug 是否Debug模式
     */
    public static void init(Context context, @Nullable String rootDir, boolean isDebug) {
        Library instance = getInstance();
        instance.context = context;
        instance.rootDir = rootDir;
        instance.isDebug = isDebug;
        instance.buildType = isDebug ? "debug" : "release";
        LogConfig.initLogger(context, isDebug, isDebug);
        CrashHandler.initCrashHandler(context, isDebug);
    }

    /**
     * 初始化分词词典
     */
    public static void initAnalyzer(Context context) {
        Analyzer.init(context);
    }

    /**
     * 初始化机器人
     *
     * @param manage 机器人管理器
     * @param voice  语音交互操作
     */
    public static void initRobot(Context context, Manager manage, WholeVoice voice) {
        Robot.init(context, manage, voice);
    }

    /**
     * 初始化数据库
     *
     * @param databaseVersion 数据库版本
     * @param databaseBeans   映射字节码
     */
    public static void initDatabase(Context context, int databaseVersion, List<Class<?>> databaseBeans) {
        Library instance = getInstance();
        if (databaseBeans != null && databaseBeans.size() > 0) {
            instance.databaseVersion = databaseVersion;
            instance.databaseBeans = databaseBeans;
            DatabaseHelper.initDatabaseHelper(context);
        }
    }

    /**
     * 初始化组件路由
     *
     * @param context 上下文
     */
    public static void initRoute(Context context) {
        Route.initRoute(context);
    }

    /**
     * 释放资源
     *
     * @param context 上下文
     */
    public static void release(Context context) {
        Robot.release(context);
    }

    public Context getContext() {
        return context;
    }

    public String getRootDir() {
        return rootDir;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getBuildType() {
        return buildType;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public List<Class<?>> getDatabaseBeans() {
        return databaseBeans;
    }
}
