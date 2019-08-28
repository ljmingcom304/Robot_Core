package com.baidu.cloud.videoplayer.demo.info;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 视频信息类
 * @author baidu
 *
 */
public class VideoInfo implements Parcelable {
    
    private String title = "";
    private String url = "";
    private String imageUrl = "";
    private String[] preImages = new String[16];
    private boolean canDelete = true;
    
    private int demoEnable = 1;
    private int inputType;
    private String[] inputList = new String[3];
    private String[] mediasDes = new String[3];

    public VideoInfo(String title, String url) {
        super();
        this.title = title;
        this.url = url;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String[] getPreImages() {
        return preImages;
    }
    public void setPreImages(String[] preImages) {
        this.preImages = preImages;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public void setDemoEnable(int demoEnable) {
        this.demoEnable = demoEnable;
    }

    public int getDemoEnable() {
        return demoEnable;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputList(String[] inputList) {
        this.inputList = inputList;
    }

    public String[] getInputList() {
        return inputList;
    }

    public String[] getMediasDes() {
        return mediasDes;
    }

    public void setMediasDes(String[] mediasDes) {
        this.mediasDes = mediasDes;
    }

    /**
     * 方便持久化
     * @return
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("url", url);
            json.put("title", title);
            json.put("imageUrl", imageUrl);
            json.put("preImages", preImages);
            json.put("canDelete", canDelete);
            json.put("demoEnable", demoEnable);
            json.put("inputType", inputType);
            json.put("inputList", inputList);
            json.put("mediasDes", mediasDes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
    
    /**
     * 从持久化恢复
     * @param json
     * @return
     */
    public static VideoInfo fromJson(JSONObject json) {
        VideoInfo info = null;
        try {
            info = new VideoInfo(json.getString("title"), json.getString("url"));
            info.setImageUrl(json.optString("imageUrl", ""));
            info.setPreImages((String[]) json.opt("preImages"));
            info.setCanDelete(json.optBoolean("canDelete", true));
            info.setDemoEnable(json.optInt("demoEnable"));
            info.setInputType(json.optInt("inputType", 0));
            info.setInputList((String[]) json.opt("inputList"));
            info.setMediasDes((String[]) json.opt("mediasDes"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    
    /**
     * 以下三个函数或成员，都属于Parcelable接口。是为了方便通过Intent传输准备的。
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Intent putExtra时，会调用的函数
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(imageUrl);
        dest.writeStringArray(preImages);
        boolean[] boolArray = new boolean[1];
        boolArray[0] = this.canDelete;
        dest.writeBooleanArray(boolArray);
        dest.writeInt(demoEnable);
        dest.writeInt(inputType);
        dest.writeStringArray(inputList);
        dest.writeStringArray(mediasDes);

        
    }
    
    /**
     * Intent getExtra时，
     */
    public static final Parcelable.Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {

        @Override
        public VideoInfo createFromParcel(Parcel source) {
            String title = source.readString();
            String url = source.readString();
            String imageUrl = source.readString();
            String[] preImages = new String[16];
            source.readStringArray(preImages);
            boolean[] boolArray = new boolean[1];
            source.readBooleanArray(boolArray);
            int demoEnable = source.readInt();
            int  inputType = source.readInt();
            String[] inputList = new String[3];
            source.readStringArray(inputList);
            String[] mediasDes = new String[3];
            source.readStringArray(mediasDes);

            VideoInfo p = new VideoInfo(title, url);
            p.setImageUrl(imageUrl);
            p.setPreImages(preImages);
            p.setCanDelete(boolArray[0]);
            p.setDemoEnable(demoEnable);
            p.setInputType(inputType);
            p.setInputList(inputList);
            p.setMediasDes(mediasDes);
            return p;
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}
