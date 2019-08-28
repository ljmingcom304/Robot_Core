/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.cloud.videoplayer.demo;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.baidu.cloud.media.player.IMediaPlayer;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BDCloudMediaPlayerTest extends BDCloudMediaPlayerTestBase {
    private static final String TAG = "BDCloudMediaPlayerTest";

    @Test
    public void testFirstFrameShowTime() throws Throwable {
        long first = testFirstFrameShowTime("http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8");
        Log.i(TAG, "testFirstFrameShowTime: first video used " + first + " mses");
        long second = testFirstFrameShowTime("http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8");
        Log.i(TAG, "testFirstFrameShowTime: second video used " + second + " mses");
    }

    public long testFirstFrameShowTime(final String url) throws Throwable {
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mOnPrepareCalled.signal();
            }
        });
        final long start = System.currentTimeMillis();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVideoView.setVideoPath(url);
            }
        });
        mOnPrepareCalled.waitForSignal();
        long time = System.currentTimeMillis() - start;
        mVideoView.stopPlayback();
        return time;
    }

    public void testSeekDoneTime() throws Throwable {
        long first = testSeekDoneTime("http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8");
        Log.i(TAG, "testSeekDoneTime: first video used " + first + " mses to seek");
//        long second = testFirstFrameShowTime("http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8");
//        Log.i(TAG, "testFirstFrameShowTime: second video used " + second + " mses to seek");
    }

    public long testSeekDoneTime(final String url) throws Throwable {
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                Log.d(TAG, "onPrepared: ");
                mVideoView.start();
                mOnPrepareCalled.signal();
            }
        });
        mVideoView.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer mp) {
                Log.d(TAG, "onSeekComplete: ");
                mOnSeekCompleteCalled.signal();
            }
        });
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVideoView.setVideoPath(url);
            }
        });
        mOnPrepareCalled.waitForSignal();
        final long start = System.currentTimeMillis();
        mVideoView.seekTo(30000);
        mOnSeekCompleteCalled.waitForSignal();
        long time = System.currentTimeMillis() - start;
        mVideoView.stopPlayback();
        return time;
    }

}