/*
 * Copyright (C) 2011 The Android Open Source Project
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

import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.baidu.cloud.videoplayer.widget.BDCloudVideoView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.logging.Logger;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Base class for tests which use MediaPlayer to play audio or video.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BDCloudMediaPlayerTestBase {

    @Rule
    public ActivityTestRule<PlayerTestActivity> mActivityRule =
            new ActivityTestRule<>(PlayerTestActivity.class, false, true);

    private static final Logger LOG = Logger.getLogger(BDCloudMediaPlayerTestBase.class.getName());
    protected static final int SLEEP_TIME = 1000;
    protected static final int LONG_SLEEP_TIME = 6000;
    protected static final int STREAM_RETRIES = 20;

    protected Monitor mOnVideoSizeChangedCalled = new Monitor();
    protected Monitor mOnVideoRenderingStartCalled = new Monitor();
    protected Monitor mOnBufferingUpdateCalled = new Monitor();
    protected Monitor mOnPrepareCalled = new Monitor();
    protected Monitor mOnSeekCompleteCalled = new Monitor();
    protected Monitor mOnCompletionCalled = new Monitor();
    protected Monitor mOnInfoCalled = new Monitor();
    protected Monitor mOnErrorCalled = new Monitor();
    protected Context mContext;
    protected Resources mResources;
    protected BDCloudVideoView mVideoView = null;
    protected PlayerTestActivity mActivity;

    /**
     * 这个方法会在测试开始前被调用
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        getInstrumentation().waitForIdleSync();
        mActivity = mActivityRule.getActivity();
        mVideoView = mActivity.getmVV();
        mContext = InstrumentationRegistry.getTargetContext();
        mResources = mContext.getResources();
    }

    /**
     * 这个方法会在测试完成后被调用
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        mActivity = null;
    }

    public static class Monitor {
        private int numSignal;

        public synchronized void reset() {
            numSignal = 0;
        }

        public synchronized void signal() {
            numSignal++;
            notifyAll();
        }

        public synchronized boolean waitForSignal() throws InterruptedException {
            return waitForCountedSignals(1) > 0;
        }

        public synchronized int waitForCountedSignals(int targetCount) throws InterruptedException {
            while (numSignal < targetCount) {
                wait();
            }
            return numSignal;
        }

        public synchronized boolean waitForSignal(long timeoutMs) throws InterruptedException {
            return waitForCountedSignals(1, timeoutMs) > 0;
        }

        public synchronized int waitForCountedSignals(int targetCount, long timeoutMs)
                throws InterruptedException {
            if (timeoutMs == 0) {
                return waitForCountedSignals(targetCount);
            }
            long deadline = System.currentTimeMillis() + timeoutMs;
            while (numSignal < targetCount) {
                long delay = deadline - System.currentTimeMillis();
                if (delay <= 0) {
                    break;
                }
                wait(delay);
            }
            return numSignal;
        }

        public synchronized boolean isSignalled() {
            return numSignal >= 1;
        }

        public synchronized int getNumSignal() {
            return numSignal;
        }
    }
}