package com.mmednet.baidu.tts;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.mmednet.baidu.utils.FileUtils;

import java.io.IOException;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


/**
 * 在线SDK不会使用这个文件
 * Created by fujiayi on 2017/5/19.
 */

public class OfflineResource implements IOfflineResourceConst {

    private AssetManager assets;
    private String destPath;

    private String textFilename;
    private String modelFilename;

    private static HashMap<String, Boolean> mapInitied = new HashMap<String, Boolean>();

    public OfflineResource(Context context, String voiceType) {
        context = context.getApplicationContext();
        this.assets = context.getApplicationContext().getAssets();
        this.destPath = FileUtils.createTmpDir(context);
        setOfflineVoiceType(voiceType);
    }

    public String getModelFilename() {
        return modelFilename;
    }

    public String getTextFilename() {
        return textFilename;
    }

    public void setOfflineVoiceType(String voiceType) {
        String text = TEXT_MODEL;
        String model;
        if (VOICE_MALE.equals(voiceType)) {
            model = VOICE_MALE_MODEL;
        } else if (VOICE_FEMALE.equals(voiceType)) {
            model = VOICE_FEMALE_MODEL;
        } else if (VOICE_DUXY.equals(voiceType)) {
            model = VOICE_DUXY_MODEL;
        } else if (VOICE_DUYY.equals(voiceType)) {
            model = VOICE_DUYY_MODEL;
        } else {
            throw new RuntimeException("voice type is not in list");
        }
        textFilename = copyAssetsFile(text);
        modelFilename = copyAssetsFile(model);
    }


    private String copyAssetsFile(String sourceFilename) {
        String destFilename = destPath + "/" + sourceFilename;
        boolean recover = false;
        Boolean existed = mapInitied.get(sourceFilename); // 启动时完全覆盖一次
        if (existed == null || !existed) {
            recover = true;
        }
        try {
            FileUtils.copyFromAssets(assets, sourceFilename, destFilename, recover);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "文件复制成功：" + destFilename);
        return destFilename;
    }


}
