package com.baidu.cloud.videoplayer.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.baidu.cloud.videoplayer.demo.info.DownloadObserverManager;
import com.baidu.cloud.videoplayer.demo.info.SampleObserver;
import com.baidu.cloud.videoplayer.demo.info.SharedPrefsStore;
import com.baidu.cloud.videoplayer.demo.info.VideoInfo;
import com.baidu.cloud.videoplayer.demo.popview.CustomAlertWindow;
import com.baidu.cloud.videoplayer.demo.popview.MorePopWindow;
import com.baidu.cloud.media.download.DownloadableVideoItem;
import com.baidu.cloud.media.download.VideoDownloadManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Title:PlayerMainActivity
 * <p>
 * Description:https://cloud.baidu.com/doc/VOD/index.html
 * </p>
 * Author Jming.L
 * Date 2019/8/28 15:48
 */
public class PlayerMainActivity extends Activity {
    private static final String TAG = "PlayerMainActivity";
    private ListView listView;
    private ArrayList<VideoInfo> listData = new ArrayList<VideoInfo>();

    public static final String SAMPLE_USER_NAME = "sampleUser";
    VideoDownloadManager downloadManagerInstance;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(0xfff7f7f7);
        }
        setContentView(R.layout.activity_main);

        downloadManagerInstance = VideoDownloadManager.getInstance(PlayerMainActivity.this, PlayerMainActivity.SAMPLE_USER_NAME);

        listView = (ListView) this.findViewById(R.id.lv_video_list);
        listView.setAdapter(adapter);
        initOtherUI();
    }

    private void initOtherUI() {
        ImageButton ibMore = (ImageButton) this.findViewById(R.id.ibtn_more);
        ibMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MorePopWindow popWindow = MorePopWindow.createMorePopWindow(PlayerMainActivity.this);
                popWindow.showPopupWindow(findViewById(R.id.rl_top_bar));
            }

        });
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        refreshData();
        super.onResume();

    }

    public void refreshData() {
        listData = SharedPrefsStore.getAllMainVideoFromSP(this);
        adapter.notifyDataSetChanged();
    }

    /**
     * 注：以下adapter仅为简单实现，实际项目中需要进行优化
     */
    private BaseAdapter adapter = new BaseAdapter() {

        private LayoutInflater mInflater;

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(PlayerMainActivity.this);
            }
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_of_list_video, null);
            }
            final VideoInfo info = listData.get(position);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.tv_item_desc);
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            ImageButton ibtnDelete = (ImageButton) convertView.findViewById(R.id.ibtn_item_delete);
            ImageButton ibtnDownload = (ImageButton) convertView.findViewById(R.id.ibtn_item_download);
            
            if (info.getImageUrl() != null && !info.getImageUrl().equals("")) {
                // fetch image from assets
                // if your image from url, you need to fetch image async
                InputStream ims;
                try {
                    ims = getAssets().open(info.getImageUrl());
                    Drawable iconDrawable = Drawable.createFromStream(ims, null);
                    ivIcon.setImageDrawable(iconDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            } else {
                ivIcon.setImageResource(R.drawable.item_default_icon);
            }
            tvTitle.setText(info.getTitle());
            tvDesc.setText(info.getUrl());
            if (info.isCanDelete()) {
                ibtnDelete.setVisibility(View.VISIBLE);
                ibtnDelete.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String msgString = "确定要删除<" + info.getTitle() + ">这个视频资源嘛？";
                        String yesString = "确定";
                        String noString = "取消";
                        View.OnClickListener yesListener = new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                SharedPrefsStore.deleteMainVideo(PlayerMainActivity.this, info);
                                refreshData();
                            }
                        };
                        CustomAlertWindow.showAlertWindow(PlayerMainActivity.this, msgString, yesString, noString,
                                yesListener, null);
                    }

                });
            } else {
                ibtnDelete.setVisibility(View.INVISIBLE);
            }

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    boolean isSimple = SharedPrefsStore.isControllBarSimple(PlayerMainActivity.this);
                    if (isSimple) {
                        // SimplePlayActivity简易播放窗口，便于快速了解播放流程
                        intent = new Intent(PlayerMainActivity.this, SimplePlayActivity.class);
                    } else {
                        // AdvancedPlayActivity高级播放窗口，内含丰富的播放控制逻辑
                        intent = new Intent(PlayerMainActivity.this, AdvancedPlayActivity.class);
                    }
                    intent.putExtra("videoInfo", info);
                    startActivity(intent);
                }

            });

            if (info.getUrl() != null && info.getUrl().endsWith(".m3u8")) {
                ibtnDownload.setVisibility(View.VISIBLE);
                ibtnDownload.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //
                        DownloadableVideoItem item = downloadManagerInstance
                                .getDownloadableVideoItemByUrl(info.getUrl());
                        if (item != null) {
                            // already
                            Toast.makeText(PlayerMainActivity.this, "该资源已经在缓存列表，请点击右上角「本地缓存」查看", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPrefsStore.addToCacheVideo(PlayerMainActivity.this, info);
                            SampleObserver sampleObs = new SampleObserver();
                            DownloadObserverManager.addNewObserver(info.getUrl(), sampleObs);
                            downloadManagerInstance.startOrResumeDownloader(info.getUrl(), sampleObs);
                            Toast.makeText(PlayerMainActivity.this, "开始缓存，请点击右上角「本地缓存」查看进度", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            } else {
                ibtnDownload.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

    };
}