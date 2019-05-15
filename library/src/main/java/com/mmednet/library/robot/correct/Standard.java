package com.mmednet.library.robot.correct;

import android.util.Log;

import java.util.ArrayList;

/**
 * Title:Standard
 * <p>
 * Description:文字纠错
 * </p>
 * Author Jming.L
 * Date 2017/9/4 11:36
 */
public class Standard {

    private static final String TAG = "Standard";
    static final String FILE_NAME = "voice2text.xml";
    private ArrayList<TextBean> texts;

    public Standard() {
        this(FILE_NAME);
    }

    /**
     * 文字校正器
     *
     * @param fileName 位于assets目录下的xml文件名
     */
    public Standard(String fileName) {
        texts = XmlParser.parse(fileName);
    }

    /**
     * 纠正文字
     *
     * @param content 纠正内容
     * @return 纠正结果
     */
    public String correct(String content) {
        for (int i = 0; texts != null && i < texts.size(); i++) {
            TextBean text = texts.get(i);

            ArrayList<String> values = text.getValues();
            for (int j = 0; j < values.size(); j++) {
                String name = values.get(j);
                if (content.contains(name)) {
                    String key = text.getKey();
                    content = content.replaceAll(name, key);
                    Log.i(TAG, "Content:" + content + " Key:" + key + " Name:" + name);
                }
            }
        }
        return content;
    }
}
