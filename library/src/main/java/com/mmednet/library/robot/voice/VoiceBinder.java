package com.mmednet.library.robot.voice;

import com.mmednet.library.layout.Layout;
import com.mmednet.library.log.Logger;

/**
 * Title:VoiceBinder
 * <p>
 * Description:语音控制构造器
 * </p>
 * Author Jming.L
 * Date 2018/3/22 10:34
 */
public class VoiceBinder {

    private static final String TAG = "VoiceBinder";

    public static void init(Layout layout) {
        if (layout == null) {
            Logger.e(TAG, "Layout is null!");
            return;
        }
        Class<? extends Layout> layoutClass = layout.getClass();
        BindVoice bindVoice = layoutClass.getAnnotation(BindVoice.class);
        if (bindVoice != null) {
            Class<? extends PieceVoice> clazz = bindVoice.bind();
            try {
                PieceVoice voice = clazz.newInstance();
                //noinspection unchecked
                voice.bind(layout);
                Logger.i(TAG, layoutClass.getSimpleName() + " is bound to " + clazz.getSimpleName());
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }

}
