package com.mmednet.library.analyze.cfg;

import android.content.Context;

import com.mmednet.library.Library;

import java.util.ArrayList;
import java.util.List;


public class DefaultConfig implements Configuration {

    private static final String PATH_DIC = "assets/";
    //主词典路径
    private static final String PATH_DIC_MAIN = PATH_DIC + "word_main.dic";
    //量词词典路径
    private static final String PATH_DIC_QUANTIFIER = PATH_DIC + "word_quantifier.dic";
    //停用词词典路径
    private static final String PATH_DIC_STOP = PATH_DIC + "word_stop.dic";
    //扩展词词典路径
    private static final String PATH_DIC_EXT = PATH_DIC + "word_ext.dic";

    private boolean useSmart;
    private Context context;

    private DefaultConfig() {
    }

    private enum Singleton {
        INSTANCE;
        private DefaultConfig config;

        Singleton() {
            config = new DefaultConfig();
        }

        public DefaultConfig getInstance() {
            return config;
        }
    }

    public static DefaultConfig getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public List<String> getExtDictionarys() {
        ArrayList<String> list = new ArrayList<>();
        list.add(PATH_DIC_EXT);
        return list;
    }

    @Override
    public List<String> getExtStopWordDictionarys() {
        ArrayList<String> list = new ArrayList<>();
        list.add(PATH_DIC_STOP);
        return list;
    }

    @Override
    public String getMainDictionary() {
        return PATH_DIC_MAIN;
    }

    @Override
    public String getQuantifierDicionary() {
        return PATH_DIC_QUANTIFIER;
    }

    @Override
    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    @Override
    public boolean useSmart() {
        return useSmart;
    }

}
