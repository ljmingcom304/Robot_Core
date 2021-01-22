package com.mmednet.face.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.baidu.aip.ImageFrame;
import com.baidu.idl.facesdk.FaceInfo;
import com.mmednet.face.R;


/**
 * Title: Robot_Library
 * <p>
 * Description:把面部识别,提示信息的判断封装为一个工具类
 * </p>
 *
 * @author Zsy
 * @date 2019/3/29 14:55
 */

public class RemindHelper {

    private static final double ANGLE = 15;//偏转角度范围

    private String detect_zoom_out;
    private String detect_zoom_in;
    private String detect_head_up;
    private String detect_head_down;
    private  String detect_head_left;
    private String detect_head_right;
    private String detect_low_light;
    private String detect_face_in;
    private String detect_keep;
    private String detect_occ_right_eye;
    private String detect_occ_left_eye;
    private String detect_occ_nose;
    private  String detect_occ_mouth;
    private String detect_right_contour;
    private String detect_left_contour;
    private String detect_chin_contour;

    public String getString() {
        return "RemindHelper{" +
                "detect_zoom_out='" + detect_zoom_out + '\'' +
                ", detect_zoom_in='" + detect_zoom_in + '\'' +
                ", detect_head_up='" + detect_head_up + '\'' +
                ", detect_head_down='" + detect_head_down + '\'' +
                ", detect_head_left='" + detect_head_left + '\'' +
                ", detect_head_right='" + detect_head_right + '\'' +
                ", detect_low_light='" + detect_low_light + '\'' +
                ", detect_face_in='" + detect_face_in + '\'' +
                ", detect_keep='" + detect_keep + '\'' +
                ", detect_occ_right_eye='" + detect_occ_right_eye + '\'' +
                ", detect_occ_left_eye='" + detect_occ_left_eye + '\'' +
                ", detect_occ_nose='" + detect_occ_nose + '\'' +
                ", detect_occ_mouth='" + detect_occ_mouth + '\'' +
                ", detect_right_contour='" + detect_right_contour + '\'' +
                ", detect_left_contour='" + detect_left_contour + '\'' +
                ", detect_chin_contour='" + detect_chin_contour + '\'' +
                '}';
    }

    public RemindHelper(Context context) {
        Resources res = context.getResources();
        detect_zoom_out = res.getString(R.string.detect_zoom_out);
        detect_zoom_in = res.getString(R.string.detect_zoom_in);
        detect_head_up = res.getString(R.string.detect_head_up);
        detect_head_down = res.getString(R.string.detect_head_down);
        detect_head_left = res.getString(R.string.detect_head_left);
        detect_head_right = res.getString(R.string.detect_head_right);
        detect_low_light = res.getString(R.string.detect_low_light);
        detect_face_in = res.getString(R.string.detect_face_in);
        detect_keep = res.getString(R.string.detect_keep);
        detect_occ_right_eye = res.getString(R.string.detect_occ_right_eye);
        detect_occ_left_eye = res.getString(R.string.detect_occ_left_eye);
        detect_occ_nose = res.getString(R.string.detect_occ_nose);
        detect_occ_mouth = res.getString(R.string.detect_occ_mouth);
        detect_right_contour = res.getString(R.string.detect_right_contour);
        detect_left_contour = res.getString(R.string.detect_left_contour);
        detect_chin_contour = res.getString(R.string.detect_chin_contour);
        Log.i("fffff", "初始化: "+getString());
    }

    public String remind(int status, FaceInfo[] infos, ImageFrame imageFrame) {

        String str = null;
        if (status == 0) {
            if (infos != null && infos[0] != null) {
                FaceInfo info = infos[0];
                //远近
                if (info != null && imageFrame != null) {
                    if (info.mWidth >= (0.9 * imageFrame.getWidth())) {
                        str = detect_zoom_out;
                    } else if (info.mWidth <= 0.4 * imageFrame.getWidth()) {
                        str = detect_zoom_in;
                    }
                }
                //上下
                if (info != null) {
                    if (info.headPose[0] >= ANGLE) {
                        str = detect_head_up;
                    } else if (info.headPose[0] <= -ANGLE) {
                        str = detect_head_down;
                    } else {
                    }

                    //左右
                    if (info.headPose[1] >= ANGLE) {
                        str = detect_head_left;
                    } else if (info.headPose[1] <= -ANGLE) {
                        str = detect_head_right;
                    }
                }
            }
        } else if (status == 1) {
            str = detect_head_up;
        } else if (status == 2) {
            str = detect_head_down;
        } else if (status == 3) {
            str = detect_head_left;
        } else if (status == 4) {
            str = detect_head_right;
        } else if (status == 5) {
            str = detect_low_light;
        } else if (status == 6) {
            str = detect_face_in;
        } else if (status == 7) {
            str = detect_face_in;
        } else if (status == 10) {
            str = detect_keep;
        } else if (status == 11) {
            str = detect_occ_right_eye;
        } else if (status == 12) {
            str = detect_occ_left_eye;
        } else if (status == 13) {
            str = detect_occ_nose;
        } else if (status == 14) {
            str = detect_occ_mouth;
        } else if (status == 15) {
            str = detect_right_contour;
        } else if (status == 16) {
            str = detect_left_contour;
        } else if (status == 17) {
            str = detect_chin_contour;
        }
        return str;
    }
}
