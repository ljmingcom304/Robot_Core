package com.mmednet.zxing.manager;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class ResultLauncher {

    private static final String TAG = ResultLauncher.class.getSimpleName();
    private Context mContext;
    private ResultFragment mFragment;

    public static ResultLauncher init(FragmentActivity activity) {
        return new ResultLauncher(activity);
    }

    private ResultLauncher(FragmentActivity activity) {
        mContext = activity;
        mFragment = getFragment(activity);
    }

    private ResultFragment getFragment(FragmentActivity activity) {
        ResultFragment fragment = findFragment(activity);
        if (fragment == null) {
            fragment = ResultFragment.newInstance();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private ResultFragment findFragment(FragmentActivity activity) {
        return (ResultFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    public void startActivityForResult(Class<?> clazz, Callback callback) {
        Intent intent = new Intent(mContext, clazz);
        startActivityForResult(intent, callback);
    }

    public void startActivityForResult(Intent intent, Callback callback) {
        mFragment.startActivityForResult(intent, callback);
    }

    public interface Callback {
        void onActivityResult(int resultCode, Intent data);
    }

}
