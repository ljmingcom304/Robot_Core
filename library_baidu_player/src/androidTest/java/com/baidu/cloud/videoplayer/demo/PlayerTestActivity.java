package com.baidu.cloud.videoplayer.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.baidu.cloud.media.player.IMediaPlayer;
import com.baidu.cloud.videoplayer.controller.SimpleMediaController;
import com.baidu.cloud.videoplayer.demo.info.SharedPrefsStore;
import com.baidu.cloud.videoplayer.widget.BDCloudVideoView;
import com.baidu.cloud.videoplayer.widget.BDCloudVideoView.PlayerState;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerTestActivity extends Activity implements IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener,
        BDCloudVideoView.OnPlayerStateListener {
    private static final String TAG = "PlayerTestActivity";

    /**
     * 您的AK 请到http://console.bce.baidu.com/iam/#/iam/accesslist获取
     */
    private String ak = ""; // 请录入您的AK !!!

    private BDCloudVideoView mVV = null;
    private SimpleMediaController mediaController = null;
    private RelativeLayout mViewHolder = null;

    private Timer barTimer;

    /**
     * 记录播放位置
     */
    private int mLastPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_simple_video_playing);


        initUI();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        mediaController = (SimpleMediaController) findViewById(R.id.media_controller_bar);
        /**
         * 设置ak
         */
        BDCloudVideoView.setAK(ak);

        mVV = new BDCloudVideoView(this);
        if (SharedPrefsStore.isPlayerFitModeCrapping(getApplicationContext())) {
            mVV.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        } else {
            mVV.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        }
        
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(-1, -1);
        rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mViewHolder.addView(mVV, rllp);

        /**
         * 注册listener
         */
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnErrorListener(this);
        mVV.setOnInfoListener(this);
        mVV.setOnBufferingUpdateListener(this);
        mVV.setOnPlayerStateListener(this);

        mediaController.setMediaPlayerControl(mVV);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
        if (mVV != null) {
            mVV.enterForeground();
        }
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        if (mVV != null) {
            mVV.enterBackground();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVV != null) {
            mVV.stopPlayback();
        }
        Log.v(TAG, "onDestroy");
    }

    /**
     * 检测'点击'空白区的事件，若播放控制控件未显示，设置为显示，否则隐藏。
     * 
     * @param v
     */
    public void onClickEmptyArea(View v) {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        if (this.mediaController != null) {
            if (mediaController.getVisibility() == View.VISIBLE) {
                mediaController.hide();
            } else {
                mediaController.show();
                hideOuterAfterFiveSeconds();
            }
        }
    }

    private void hideOuterAfterFiveSeconds() {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        barTimer = new Timer();
        barTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (mediaController != null) {
                    mediaController.getMainThreadHandler().post(new Runnable() {

                        @Override
                        public void run() {
                            mediaController.hide();
                        }

                    });
                }
            }

        }, 5 * 1000);

    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        // restart player?
        
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        if (mediaController != null && mVV != null) {
            mediaController.onTotalCacheUpdate(percent * mVV.getDuration() / 100);
        }
    }
    
    @Override
    public void onPlayerStateChanged(PlayerState nowState) {
        if (mediaController != null) {
            mediaController.changeState();
        }
    }

    public BDCloudVideoView getmVV() {
        return mVV;
    }
}
