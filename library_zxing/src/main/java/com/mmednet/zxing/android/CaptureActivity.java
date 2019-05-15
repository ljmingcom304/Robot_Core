package com.mmednet.zxing.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.mmednet.zxing.R;
import com.mmednet.zxing.manager.CaptureManager;
import com.mmednet.zxing.view.ViewfinderView;


/**
 * Title:CaptureActivity
 * <p>
 * Description:扫一扫
 * </p>
 * Author Jming.L
 * Date 2018/12/14 9:57
 */
public class CaptureActivity extends FragmentActivity {

    private SurfaceView mSvSurface;
    private ViewfinderView mVvFinder;
    private TextView mTvBack;
    private TextView mTvLight;
    private TextView mTvPhoto;

    private CaptureManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxing_activity_capture);
        initView();
    }


    private void initView() {
        mSvSurface = findViewById(R.id.sv_surface);
        mVvFinder = findViewById(R.id.vv_finder);
        mTvBack = findViewById(R.id.tv_back);
        mTvPhoto = findViewById(R.id.tv_photo);
        mTvLight = findViewById(R.id.tv_light);

        mManager = new CaptureManager(this, mSvSurface, mVvFinder);

        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //切换闪光灯
        mTvLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.switchFlashLight();
            }
        });

        //打开相册
        mTvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.decodePhoto();
            }
        });
    }


}
