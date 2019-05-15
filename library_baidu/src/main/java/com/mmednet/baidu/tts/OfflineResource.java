package com.mmednet.baidu.tts;

import android.content.Context;
import android.content.res.AssetManager;

import com.mmednet.baidu.utils.FileUtils;


/**
 * Title:OfflineResource
 * <p>
 * Description:Sdcard中不存在离线资源，需要将离线资源复制到Sdcard中
 * </p>
 * Author Jming.L
 * Date 2018/8/1 11:09
 */
class OfflineResource {

    public static final String VOICE_FEMALE = "F";

    public static final String VOICE_MALE = "M";


    public static final String VOICE_DUYY = "Y";

    public static final String VOICE_DUXY = "X";

    private AssetManager assets;
    private String destPath;

    private String textFilename;
    private String modelFilename;

    public OfflineResource(Context context, String voiceType) {
        context = context.getApplicationContext();
        this.assets = context.getApplicationContext().getAssets();
        this.destPath = FileUtils.createTmpDir(context);
        setOfflineVoiceType(voiceType);
    }

    /**
     * 声学模型文件
     */
    public String getModelFilename() {
        return modelFilename;
    }

    /**
     * 文本模型文件
     */
    public String getTextFilename() {
        return textFilename;
    }

    public void setOfflineVoiceType(String voiceType) {
        String text = "bd_etts_text.dat";
        String model;
        if (VOICE_MALE.equals(voiceType)) {
            model = "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat";
        } else if (VOICE_FEMALE.equals(voiceType)) {
            model = "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat";
        } else if (VOICE_DUXY.equals(voiceType)) {
            model = "bd_etts_common_speech_yyjw_mand_eng_high_am-mix_v3.0.0_20170512.dat";
        } else if (VOICE_DUYY.equals(voiceType)) {
            model = "bd_etts_common_speech_as_mand_eng_high_am_v3.0.0_20170516.dat";
        } else {
            throw new RuntimeException("voice type is not in list");
        }
        textFilename = destPath + "/" + text;
        modelFilename = destPath + "/" + model;
        FileUtils.copyFromAssets(assets, text, textFilename);
        FileUtils.copyFromAssets(assets, model, modelFilename);
    }

}
