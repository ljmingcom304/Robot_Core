package com.mmednet.library.robot.engine.action;

import com.mmednet.library.robot.engine.WholeVoice;
import com.mmednet.library.robot.engine.operation.Command;
import com.mmednet.library.util.StringUtils;
import com.mmednet.library.util.UIUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Title:Action
 * <p>
 * Description:通过语音指令执行指定动作
 * </p>
 * Author Jming.L
 * Date 2017/9/27 9:59
 */
public class Action {

    private static final String TAG = "Action";
    private HashMap<String, Method> actionAndMethod;
    private WholeVoice voice;

    public Action(WholeVoice voice) {
        this.voice = voice;
        actionAndMethod = new HashMap<>();
        Method[] methods = voice.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Command.class)) {
                method.setAccessible(true);
                Command command = method.getAnnotation(Command.class);
                int[] actionIds = command.actionId();
                for (int act : actionIds) {
                    String action = UIUtils.getString(act);
                    actionAndMethod.put(action, method);
                }
                String[] actionTexts = command.actionTexts();
                for (String action : actionTexts) {
                    actionAndMethod.put(action, method);
                }
            }
        }
    }

    /**
     * 执行指定命令的操作
     *
     * @param message 待执行的命令
     * @return 是否执行成功
     */
    public boolean execute(String message) {
        for (Map.Entry<String, Method> entry : actionAndMethod.entrySet()) {
            String key = entry.getKey();
            String keyPy = StringUtils.toPinyin(key);
            String msgPy = StringUtils.toPinyin(message);
            boolean match = msgPy.contains(keyPy);
            if (match) {
                try {
                    entry.getValue().invoke(voice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
}
