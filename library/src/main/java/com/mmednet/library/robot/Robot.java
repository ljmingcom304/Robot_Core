package com.mmednet.library.robot;

import android.content.Context;

import com.mmednet.library.robot.engine.Engine;
import com.mmednet.library.robot.engine.WholeVoice;
import com.mmednet.library.robot.manage.Manager;

/**
 * Title:Robot
 * <p>
 * Description:机器人
 * </p>
 * Author Jming.L
 * Date 2017/9/4 14:54
 */
public class Robot {

    private Engine engine;
    private Manager manage;

    private Robot() {
    }

    private enum Singleton {
        INSTANCE;
        private Robot robot;

        Singleton() {
            robot = new Robot();
        }

        public Robot getInstance() {
            return robot;
        }
    }

    public static Robot getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    /**
     * 初始化资源
     *
     * @param context 上下文
     */
    public static Robot init(Context context, Manager manage, WholeVoice voice) {
        Robot robot = Robot.getInstance();
        manage.init(context);
        robot.manage = manage;
        robot.engine = new Engine(voice, manage);
        return robot;
    }

    /**
     * 释放资源
     *
     * @param context 上下文
     */
    public static void release(Context context) {
        getManage().release(context);
    }

    /**
     * 语音交互
     *
     * @return 语音交互引擎
     */
    public static Engine getEngine() {
        Robot robot = Robot.getInstance();
        return robot.engine;
    }

    /**
     * 机器人管理
     *
     * @return 机器人管理者
     */
    public static Manager getManage() {
        Robot robot = Robot.getInstance();
        return robot.manage;
    }

}
