package com.baidu.cloud.videoplayer.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cloud.media.player.BDCloudMediaPlayer;
import com.baidu.cloud.videoplayer.demo.R;
import com.baidu.cloud.videoplayer.demo.info.SharedPrefsStore;
import com.baidu.cloud.videoplayer.demo.popview.ResolutionListPopWindow;
import com.baidu.cloud.videoplayer.widget.BDCloudVideoView;
import com.baidu.cloud.videoplayer.widget.BDCloudVideoView.PlayerState;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义播放控制类
 *
 * @author baidu
 */
public class AdvancedMediaController extends RelativeLayout implements OnClickListener {

    private static final String TAG = "AdvancedMediaController";


    private ImageButton playButton;
    private ImageButton snapshotButton;
    private Button fitButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private Button resolutionButton;
    private ImageButton downloadButton;
    private TextView positionView;
    private SeekBar seekBar;
    private TextView durationView;
    private TextView infoResolutionView;
    // private TextView infoBitrateView;
    private TextView infoNetworkView;

    private String[] availableResolution = null;

    public String[] getAvailableResolution() {
        return availableResolution;
    }

    public void setAvailableResolution(String[] fetchResolution) {
        Log.d(TAG, "setAvailableResolution = " + Arrays.toString(fetchResolution));
        if (fetchResolution != null && fetchResolution.length > 1) {
            String[] availableResolutionDesc = new String[fetchResolution.length];
            for (int i = 0; i < fetchResolution.length; ++i) {
                availableResolutionDesc[i] = getDescriptionOfResolution(fetchResolution[i]);
            }
            this.availableResolution = availableResolutionDesc;
        }

    }

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public Handler getMainThreadHandler() {
        return mainThreadHandler;
    }

    private BDCloudVideoView mVideoView = null;

    private ImageView mImageView = null;
    private TextView mTimeView = null;

    private String[] mPreImages = null;

    boolean mbIsDragging = false;

    private int lastIndex = -1;
    private Bitmap lastBitmap = null;

    // private View.OnClickListener playListener;
    private View.OnClickListener preListener;
    private View.OnClickListener nextListener;
    private View.OnClickListener downloadListener;

    private Timer positionTimer;
    private static final int POSITION_REFRESH_TIME = 500;

    public AdvancedMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public AdvancedMediaController(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {

        // inflate controller bar
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.bar_advanced_media_controller, this);

        playButton = (ImageButton) layout.findViewById(R.id.ibtn_play);
        playButton.setOnClickListener(this);

        snapshotButton = (ImageButton) layout.findViewById(R.id.ibtn_snapshot);
        snapshotButton.setOnClickListener(this);

        fitButton = (Button) layout.findViewById(R.id.btn_fitmode);
        if (SharedPrefsStore.isPlayerFitModeCrapping(this.getContext())) {
            fitButton.setText("裁剪");
        } else {
            fitButton.setText("填充");
        }
        fitButton.setOnClickListener(this);

        previousButton = (ImageButton) layout.findViewById(R.id.ibtn_previous);
        previousButton.setOnClickListener(this);

        nextButton = (ImageButton) layout.findViewById(R.id.ibtn_next);
        nextButton.setOnClickListener(this);

        resolutionButton = (Button) layout.findViewById(R.id.btn_resolution);
        resolutionButton.setOnClickListener(this);

        downloadButton = (ImageButton) layout.findViewById(R.id.ibtn_download);
        downloadButton.setOnClickListener(this);

        positionView = (TextView) layout.findViewById(R.id.tv_position);
        seekBar = (SeekBar) layout.findViewById(R.id.seekbar);
        seekBar.setMax(0);
        durationView = (TextView) layout.findViewById(R.id.tv_duration);

        infoResolutionView = (TextView) layout.findViewById(R.id.tv_resolution);
        // infoBitrateView = (TextView) layout.findViewById(R.id.tv_bitrate);
        infoNetworkView = (TextView) layout.findViewById(R.id.tv_netspeed);

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mbIsDragging && mPreImages[0] != null) {
                    if (lastIndex != spriteImageIndex(progress)) {
                        lastIndex = spriteImageIndex(progress);
                        RequestOptions options = new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                        //noinspection unchecked
                        Glide.with(getContext())
                                .load(mPreImages[lastIndex])
                                .apply(options)
                                .into(target);
                    }
                    mTimeView.setText(formatMilliSecond(progress));
                    if (lastBitmap != null) {
                        int position = thumbnailPosition(progress);
                        int row = position / 3;
                        int coln = position % 3;
                        Bitmap bitmap = Bitmap.createBitmap(lastBitmap, coln * 231, row * 130, 231, 130);
                        mImageView.setImageBitmap(bitmap);
                    }
                }
                updatePostion(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mbIsDragging = true;
                if (mPreImages[0] != null) {
                    mImageView.setVisibility(VISIBLE);
                    mTimeView.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mPreImages[0] != null) {
                    mImageView.setVisibility(INVISIBLE);
                    mTimeView.setVisibility(INVISIBLE);
                }
                if (mVideoView.getDuration() > 0) {
                    // 仅非直播的视频支持拖动
                    currentPositionInMilliSeconds = seekBar.getProgress();
                    if (mVideoView != null) {
                        mVideoView.seekTo(seekBar.getProgress());
                    }
                }

                mbIsDragging = false;
            }
        });
        enableControllerBar(false);
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(@NonNull Bitmap resource,
                                    @Nullable Transition<? super Bitmap> transition) {
            lastBitmap = resource;
        }
    };

    /**
     *
     */
    public void changeState() {
        final PlayerState state = mVideoView.getCurrentPlayerState();
        Log.d(TAG, "mediaController: changeStatus=" + state.name());
        isMaxSetted = false;
        mainThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                if (state == PlayerState.STATE_IDLE || state == PlayerState.STATE_ERROR) {
                    stopPositionTimer();
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    seekBar.setEnabled(false);
                    resolutionButton.setEnabled(false);
                    updatePostion(mVideoView == null ? 0 : mVideoView.getCurrentPosition());
                    updateDuration(mVideoView == null ? 0 : mVideoView.getDuration());
                } else if (state == PlayerState.STATE_PREPARING) {
                    playButton.setEnabled(false);
//                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    seekBar.setEnabled(false);
                    resolutionButton.setEnabled(false);
                } else if (state == PlayerState.STATE_PREPARED) {

                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    seekBar.setEnabled(true);

                    // set width
                    String resolutionStr = mVideoView.getVideoWidth() + "x" + mVideoView.getVideoHeight();
                    if (mVideoView.getVideoWidth() > 0) { // 可能为音频文件，width=-1
                        infoResolutionView.setText(resolutionStr);
                    }

                    String[] variantInfo = mVideoView.getVariantInfo();
                    if (mVideoView.getMediaInputType() == BDCloudMediaPlayer.SOURCE_SWITCH_SMOOTH_MODE) {
                        variantInfo = mVideoView.getMutilMediasDsc();
                    }

                    setAvailableResolution(variantInfo);
                    updateDuration(mVideoView == null ? 0 : mVideoView.getDuration());
                    seekBar.setMax(mVideoView.getDuration());

                    resolutionButton.setEnabled(true);
                    resolutionButton.setText(getDescriptionOfResolution(resolutionStr));
                } else if (state == PlayerState.STATE_PLAYBACK_COMPLETED) {
                    stopPositionTimer();
                    seekBar.setProgress(seekBar.getMax());
                    seekBar.setEnabled(false);
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
//                    resolutionButton.setEnabled(false);
                } else if (state == PlayerState.STATE_PLAYING) {
                    startPositionTimer();
                    seekBar.setEnabled(true);
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_pause);
                } else if (state == PlayerState.STATE_PAUSED) {
                    stopPositionTimer();
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                } else if (state == PlayerState.STATE_VIDEOSIZE_CHANGED) {
                    String resolutionStr = mVideoView.getVideoWidth() + "x" + mVideoView.getVideoHeight();
                    if (mVideoView.getVideoWidth() > 0) {
                        infoResolutionView.setText(resolutionStr);
                    }
                }
            }

        });

    }

    private void startPositionTimer() {
        if (positionTimer != null) {
            positionTimer.cancel();
            positionTimer = null;
        }
        positionTimer = new Timer();
        positionTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onPositionUpdate();

                        // update speed
                        infoNetworkView.setText((mVideoView.getDownloadSpeed() >> 10) + "KB/s");
                    }
                });

            }

        }, 0, POSITION_REFRESH_TIME);
    }

    private void stopPositionTimer() {
        if (positionTimer != null) {
            positionTimer.cancel();
            positionTimer = null;
        }
    }

    /**
     * if you don't want to use Controller when still playing, invoke release to stop timer!
     */
    public void release() {
        stopPositionTimer();
    }

    /**
     * BVideoView implements VideoViewControl, so it's a BVideoView object
     * actually
     *
     * @param player
     */
    public void setMediaPlayerControl(BDCloudVideoView player) {
        mVideoView = player;
    }

    public void setViewImage(ImageView imageView, String[] preImages) {
        mImageView = imageView;
        mPreImages = preImages;
    }

    public void setTextView(TextView textView) {
        mTimeView = textView;
    }

    /**
     * Show the controller on screen. It will go away automatically after 3
     * seconds of inactivity.
     */
    public void show() {
        if (mVideoView == null) {
            return;
        }
        setProgress((int) currentPositionInMilliSeconds);

        this.setVisibility(View.VISIBLE);
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        this.setVisibility(View.GONE);
    }

    /**
     * @hide
     */
    public boolean getIsDragging() {
        return mbIsDragging;
    }

    private void updateDuration(int milliSecond) {
        if (durationView != null) {
            durationView.setText(formatMilliSecond(milliSecond));
        }
    }

    private void updatePostion(int milliSecond) {
        if (positionView != null) {
            positionView.setText(formatMilliSecond(milliSecond));
        }
    }

    private String formatMilliSecond(int milliSecond) {
        int seconds = milliSecond / 1000;
        int hh = seconds / 3600;
        int mm = seconds % 3600 / 60;
        int ss = seconds % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        return strTemp;
    }

    private int spriteImageIndex(int milliSecond) {
        return ((milliSecond / 1000) / (1 * 6));
    }

    private int thumbnailPosition(int milliSecond) {
        return (milliSecond / 1000) % (1 * 6);
    }

    private boolean isMaxSetted = false;

    /**
     * Set the max process for the seekBar, usually the lasting milliseconds
     *
     * @hide
     */
    public void setMax(int maxProgress) {
        if (isMaxSetted) {
            return;
        }
        if (seekBar != null) {
            seekBar.setMax(maxProgress);
        }
        updateDuration(maxProgress);
        if (maxProgress > 0) {
            isMaxSetted = true;
        }
    }

    /**
     * in milliSeconds
     *
     * @param progress
     * @hide
     */
    public void setProgress(int progress) {
        if (seekBar != null) {
            seekBar.setProgress(progress);
        }
    }

    /**
     * in milliseconds
     *
     * @hide
     */
    public void setCache(int cache) {
        if (seekBar != null && cache != seekBar.getSecondaryProgress()) {
            seekBar.setSecondaryProgress(cache);
        }
    }

    /**
     * @param isEnable
     * @hide
     */
    public void enableControllerBar(boolean isEnable) {
        seekBar.setEnabled(isEnable);
        playButton.setEnabled(isEnable);
    }

    private long currentPositionInMilliSeconds = 0L;

    /**
     * onPositionUpdate is invoked per 500ms
     */
    public boolean onPositionUpdate() {
        if (mVideoView == null) {
            return false;
        }
        long newPositionInMilliSeconds = mVideoView.getCurrentPosition();
//        Log.d(TAG, "onPositionUpdate getCurrentPosition=" + newPositionInMilliSeconds);
        long previousPosition = currentPositionInMilliSeconds;
        if (newPositionInMilliSeconds > 0 && !getIsDragging()) {
            currentPositionInMilliSeconds = newPositionInMilliSeconds;
        }
        if (getVisibility() != View.VISIBLE) {
            // 如果控制条不可见，则不设置进度
            return false;
        }
        if (!getIsDragging()) {
            int durationInMilliSeconds = mVideoView.getDuration();
            if (durationInMilliSeconds > 0) {
                this.setMax(durationInMilliSeconds);
                // 直播视频的duration为0，此时不设置进度
                if (previousPosition != newPositionInMilliSeconds) {
                    this.setProgress((int) newPositionInMilliSeconds);
                }
            }
        }
        return false;
    }

    public void onTotalCacheUpdate(final long milliSeconds) {
        mainThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                setCache((int) milliSeconds);
            }

        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibtn_play) {
            if (mVideoView == null) {
                Log.d(TAG, "playButton checkstatus changed, but bindView=null");
            } else {
                if (mVideoView.isPlaying()) {
                    Log.d(TAG, "playButton: Will invoke pause()");
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    mVideoView.pause();
                } else {
                    Log.d(TAG, "playButton: Will invoke resume()");
                    playButton.setBackgroundResource(R.drawable.toggle_btn_pause);
                    mVideoView.start();
                }
            }
        }

        if (v.getId() == R.id.ibtn_snapshot) {
            // take snapshot
            File sdDir = null;
            String strpath = null;
            Bitmap bitmap = null;
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS");
            String time = formatter.format(date);

            // TODO 您可以指定存储路径，以下逻辑是保存在sdcard根目录下
            sdDir = Environment.getExternalStorageDirectory();
            strpath = sdDir.toString() + "/" + time + ".jpg";
            Log.d(TAG, "snapshot save path=" + strpath);

            bitmap = mVideoView.getBitmap();
            if (bitmap != null) {
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(strpath, false);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Throwable t) {
                            Log.e(TAG, "Error occurred while saving snapshot!");
                        }
                    }
                }
                os = null;
                Toast.makeText(this.getContext(), "截图保存在sd卡根目录下, 文件名为" + strpath, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getContext(), "抱歉，当前无法截图", Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.btn_fitmode) {
            if (fitButton.getText().equals("填充")) {
                // 转为 裁剪模式：视频保持比例缩放，不留黑边，填满显示区域的两边
                mVideoView.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                fitButton.setText("裁剪");
                SharedPrefsStore.setPlayerFitMode(getContext(), true);
            } else {
                // 转为 填充模式：视频保持比例缩放，至少一边与显示区域相同，另一边有黑边
                mVideoView.setVideoScalingMode(BDCloudVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                fitButton.setText("填充");
                SharedPrefsStore.setPlayerFitMode(getContext(), false);
            }
        }

        if (v.getId() == R.id.ibtn_previous) {
            if (preListener != null) {
                preListener.onClick(v);
            }
        }

        if (v.getId() == R.id.ibtn_next) {
            if (nextListener != null) {
                nextListener.onClick(v);
            }
        }

        if (v.getId() == R.id.btn_resolution) {
            // select a resolution
            Log.v(TAG, "Show resolution clicked");
            if (this.availableResolution != null && this.availableResolution.length > 0) {

                OnItemClickListener listener = new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mVideoView != null) {
//                            mVideoView.selectResolutionType(position);
                            mVideoView.selectResolutionByIndex(position);
                            resolutionButton.setText(availableResolution[position]);
                            // infoBitrateView.setText(BVideoView.getSupportedBitrateKb()[position]
                            // / 1000 + "Kbps");
                        }
                    }

                };
                // popup window
                ResolutionListPopWindow popWindow =
                        ResolutionListPopWindow.createResolutionListPopWindow((Activity) getContext(),
                                availableResolution, listener);
                popWindow.showPopupWindow(((Activity) getContext()).findViewById(R.id.root));
            } else {
                Toast.makeText(this.getContext(), "该视频不是多码率视频(m3u8 master url)", Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.ibtn_download) {
            if (downloadListener != null) {
                downloadListener.onClick(v);
            }
        }
    }

    public void clearViewContent() {
        currentPositionInMilliSeconds = 0L;
        positionView.setText("00:00");
        durationView.setText("00:00");
        seekBar.setMax(0);
        seekBar.setProgress(0);
        this.availableResolution = null;
    }

    public View.OnClickListener getPreListener() {
        return preListener;
    }

    public void setPreListener(View.OnClickListener preListener) {
        this.preListener = preListener;
    }

    public View.OnClickListener getNextListener() {
        return nextListener;
    }

    public void setNextListener(View.OnClickListener nextListener) {
        this.nextListener = nextListener;
    }

//    public void onNetworkSpeedUpdate(final int arg0) {
// //        Log.d(TAG, "onNetworkSpeedUpdate speed=" + arg0);
//        // 右移10位，即：除以1024。因为arg0的单位为 Byte per second
//        this.mainThreadHandler.post(new Runnable() {
//
//            @Override
//            public void run() {
//                infoNetworkView.setText((arg0 >> 10) + "KB/s");
//            }
//        });
//
//    }

    public String getDescriptionOfResolution(String resolutionType) {
        String result = "未知";
        try {
            // resolutionType is like 1920x1080,3541000
            // sometimes value is ,232370 if there is no resolution desc in master m3u8 file 
            String[] cuts1 = resolutionType.trim().split(",");
            if (cuts1[0].length() > 0) {
                // cuts1[0] has resultion string
                String[] cuts2 = cuts1[0].trim().split("[xX]");
                if (cuts2.length == 2) {
                    // get the height size
                    int iResult = Integer.parseInt(cuts2[1]);
                    if (iResult <= 0) {
                        result = "未知";
                    } else if (iResult <= 120) {
                        result = "120P";
                    } else if (iResult <= 240) {
                        result = "240P";
                    } else if (iResult <= 360) {
                        result = "360P";
                    } else if (iResult <= 480) {
                        result = "480P";
                    } else if (iResult <= 800) {
                        result = "720P";
                    } else {
                        result = "1080P";
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "getDescriptionOfResolution exception:" + e.getMessage());
        }
        Log.d(TAG, "getDescriptionOfResolution orig=" + resolutionType + ";result=" + result);

        return result;
    }

    public View.OnClickListener getDownloadListener() {
        return downloadListener;
    }

    public void setDownloadListener(View.OnClickListener downloadListener) {
        this.downloadListener = downloadListener;
    }
}
