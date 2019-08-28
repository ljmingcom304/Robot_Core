package com.baidu.cloud.videoplayer.demo.info;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 此处仅为存储示例 您的项目中可能使用其他存储形式
 *
 * @author baidu
 */
public class SharedPrefsStore {
    private static final String MAIN_VIDEO_LIST_SPNAME = "video-main";
    private static final String CACHE_VIDEO_LIST_SPNAME = "video-cache";
    private static final String SETTINGS_SPNAME = "video-settings";

    private static final String KEY_VIDEOS_ARRAY = "videos";

    public static boolean addToMainVideo(Context context, VideoInfo info) {
        SharedPreferences spList = context.getSharedPreferences(MAIN_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            jsonArray.put(info.toJson());
            SharedPreferences.Editor editor = spList.edit();
            editor.putString(KEY_VIDEOS_ARRAY, jsonArray.toString());
            editor.commit();
        } catch (JSONException e1) {
            e1.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean deleteMainVideo(Context context, VideoInfo info) {
        SharedPreferences spList = context.getSharedPreferences(MAIN_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONArray newJsonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info2 = VideoInfo.fromJson(json);
                    if (info2.getUrl().equals(info.getUrl()) && info2.getTitle().equals(info.getTitle())) {
                        continue;
                    } else {
                        newJsonArray.put(json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (newJsonArray.length() == jsonArray.length()) {
                return false;
            } else {
                SharedPreferences.Editor editor = spList.edit();
                editor.putString(KEY_VIDEOS_ARRAY, newJsonArray.toString());
                editor.commit();
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public static boolean addToCacheVideo(Context context, VideoInfo info) {
        SharedPreferences spList = context.getSharedPreferences(CACHE_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            jsonArray.put(info.toJson());
            SharedPreferences.Editor editor = spList.edit();
            editor.putString(KEY_VIDEOS_ARRAY, jsonArray.toString());
            editor.commit();
        } catch (JSONException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean deleteCacheVideo(Context context, VideoInfo info) {
        SharedPreferences spList = context.getSharedPreferences(CACHE_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONArray newJsonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info2 = VideoInfo.fromJson(json);
                    if (info2.getUrl().equals(info.getUrl()) && info2.getTitle().equals(info.getTitle())) {
                        continue;
                    } else {
                        newJsonArray.put(json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (newJsonArray.length() == jsonArray.length()) {
                return false;
            } else {
                SharedPreferences.Editor editor = spList.edit();
                editor.putString(KEY_VIDEOS_ARRAY, newJsonArray.toString());
                editor.commit();
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public static ArrayList<VideoInfo> getAllMainVideoFromSP(Context context) {
        ArrayList<VideoInfo> infoList = new ArrayList<VideoInfo>();
        // add sample data first
        ArrayList<VideoInfo>  mainSampleList = getMainSampleData(context);
        for (VideoInfo info : mainSampleList) {
            if (info.getDemoEnable() > 0) {
                infoList.add(info);
            }
        }

        // add user added data
        SharedPreferences spList = context.getSharedPreferences(MAIN_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info = VideoInfo.fromJson(json);
                    if (info.getDemoEnable() > 0) {
                        infoList.add(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return infoList;
    }

    /**
     * 初次进入应用，SP无数据时，准备样例数据
     *
     * @return
     */
    public static ArrayList<VideoInfo> getMainSampleData(Context context) {
        ArrayList<VideoInfo> sampleList = new ArrayList<VideoInfo>();

        String title1 = "百度云宣传视频";
        String url1 = "http://ihgmd50mvjrejm99s3b.exp.bcevod.com"
                + "/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4";
        String[] preImages1 = {
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00001.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00002.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00003.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00004.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00005.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00006.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00007.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00008.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00009.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00010.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00011.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00012.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00013.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00014.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00015.jpg",
                "https://raw.githubusercontent.com/fushengit/imageCache/master/mda-jaqf8wrtf9et9dwp/mda-jaqf8wrtf9et9dwp.mp4-SPRITE-00016.jpg"
        };
        VideoInfo info1 = new VideoInfo(title1, url1);
        info1.setCanDelete(false);
        info1.setImageUrl("baidu_cloud_bigger.jpg");
        info1.setPreImages(preImages1);
        info1.setInputType(0);
        sampleList.add(info1);


        String title2 = "百度云宣传视频_MOV多码率无缝切换";
        String url2 = "http://bdcoud-media-player.cdn.bcebos.com/demovideo/bdcloud.mp4";
        String[] mediaItems =  new String[]{
                "http://bdcoud-media-player.cdn.bcebos.com/demovideo/all_mp4/720p.mp4",
                "http://bdcoud-media-player.cdn.bcebos.com/demovideo/all_mp4/480p.mp4",
                "http://bdcoud-media-player.cdn.bcebos.com/demovideo/all_mp4/360p.mp4"
        };
        String[] mediasDes =  new String[]{
                "1280x720,650000",
                "852x480,420000",
                "568x320,256000"
        };
        VideoInfo info2 = new VideoInfo(title2, url2);
        info2.setCanDelete(false);
        info2.setPreImages(preImages1);
        info2.setInputType(1);
        info2.setInputList(mediaItems);
        info2.setMediasDes(mediasDes);
        sampleList.add(info2);


        String title3 = "HLS多码率切换";
        String url3 = "https://bdcoud-media-player.cdn.bcebos.com/demovideo/all_hls/video.m3u8";
        VideoInfo info3 = new VideoInfo(title3, url3);
        info3.setCanDelete(false);
        info3.setInputType(0);
        info3.setDemoEnable(0);
        sampleList.add(info3);

        String title4 = "HLS多码率无缝切换";
        String url4 = "https://bdcoud-media-player.cdn.bcebos.com/demovideo/all_hls/video.m3u8";
        VideoInfo info4 = new VideoInfo(title4, url4);
        info4.setCanDelete(false);
        info4.setInputType(2);
        sampleList.add(info4);

        String title5 = "LSS服务介绍(HLS/RTMP/HTTP-FLV均可播放)";
        String url5 = "http://ihgmd50mvjrejm99s3b.exp.bcevod.com"
                + "/mda-jaqf930kehejufs5/mda-jaqf930kehejufs5.m3u8";
        VideoInfo info5 = new VideoInfo(title5, url5);
        info5.setCanDelete(false);
        info5.setInputType(0);
        info5.setDemoEnable(0);
        sampleList.add(info5);

        String title6 = "直播链接是您推流对应的播放链接";
        String url6 = "";
        VideoInfo info6 = new VideoInfo(title6, url6);
        info6.setCanDelete(false);
        info6.setDemoEnable(0);
        sampleList.add(info6);

        return sampleList;
    }

    public static ArrayList<VideoInfo> getAllCacheVideoFromSP(Context context) {
        ArrayList<VideoInfo> infoList = new ArrayList<VideoInfo>();
        SharedPreferences spList = context.getSharedPreferences(CACHE_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info = VideoInfo.fromJson(json);
                    infoList.add(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return infoList;
    }

    public static void setDefaultOrientation(Context context, boolean isPortrait) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        SharedPreferences.Editor editor = spList.edit();
        editor.putBoolean("isPortrait", isPortrait);
        editor.commit();
    }

    public static boolean isDefaultPortrait(Context context) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        return spList.getBoolean("isPortrait", true);
    }

    public static void setControllBar(Context context, boolean isSimple) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        SharedPreferences.Editor editor = spList.edit();
        editor.putBoolean("isSimple", isSimple);
        editor.commit();
    }

    public static boolean isControllBarSimple(Context context) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        return spList.getBoolean("isSimple", false);
    }

    public static void setPlayerFitMode(Context context, boolean isCrapping) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        SharedPreferences.Editor editor = spList.edit();
        editor.putBoolean("isCrapping", isCrapping);
        editor.commit();
    }

    public static boolean isPlayerFitModeCrapping(Context context) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        return spList.getBoolean("isCrapping", false);
    }
}
