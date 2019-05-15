package com.mmednet.library.analyze;

import android.content.Context;

import com.mmednet.library.analyze.cfg.DefaultConfig;
import com.mmednet.library.analyze.core.IKSegmenter;
import com.mmednet.library.analyze.core.Lexeme;
import com.mmednet.library.analyze.dic.Dictionary;
import com.mmednet.library.log.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Title:Analyzer
 * <p>
 * Description:分词器
 * </p>
 * Author Jming.L
 * Date 2017/8/31 10:28
 */
public class Analyzer {

    private Dictionary dictionary;
    private static Analyzer analyzer = null;
    private ExecutorService service;
    private static final String TAG = "Analyzer";

    private Analyzer() {
        service = Executors.newSingleThreadExecutor();
    }

    private static synchronized Analyzer getInstance() {
        if (analyzer == null) {
            analyzer = new Analyzer();
        }
        return analyzer;
    }

    public static void init(final Context context) {
        final Analyzer instance = getInstance();
        final DefaultConfig config = DefaultConfig.getInstance();
        config.setContext(context);
        instance.service.execute(new Runnable() {
            @Override
            public void run() {
                instance.dictionary = Dictionary.initial(config);
            }
        });
    }

    /**
     * 字符串分词
     *
     * @param str 被分词的字符串
     * @return 分词后结果
     */
    public static ArrayList<String> toWords(String str) {
        return toWords(str, null);
    }

    /**
     * 字符串分词
     *
     * @param str   被分词的字符串
     * @param words 动态扩展词典
     * @return 分词后结果
     */
    public static ArrayList<String> toWords(String str, Collection<String> words) {
        Analyzer instance = getInstance();
        Dictionary dictionary = instance.dictionary;
        if (dictionary == null) {
            Logger.e(TAG, "Dictionary is null");
            return null;
        }
        if (words != null) {
            dictionary.addWords(words);
        }
        StringReader reader = new StringReader(str);
        IKSegmenter iks = new IKSegmenter(reader, true);
        ArrayList<String> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        try {
            Lexeme lexeme;
            while ((lexeme = iks.next()) != null) {
                String text = lexeme.getLexemeText();
                list.add(text);
                builder.append(text);
                builder.append("|");
            }
            Logger.i(TAG, builder.toString());
            iks.reset(reader);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
