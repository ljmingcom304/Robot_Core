package com.mmednet.library.layout;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Title:ActivityLauncher
 * <p>
 * Description:直接接受Activity返回结果
 * </p>
 * Author Jming.L
 * Date 2019/1/10 17:26
 */
public class ActivityLauncher {

    private static final String TAG = "ActivityLauncher";
    private Context mContext;
    private LauncherFragment mFragment;

    public interface Callback {
        void onActivityResult(int resultCode, Intent data);
    }

    public static ActivityLauncher init(FragmentActivity activity) {
        return new ActivityLauncher(activity);
    }

    private ActivityLauncher(FragmentActivity activity) {
        mContext = activity;
        mFragment = getFragment(activity);
    }

    private LauncherFragment getFragment(FragmentActivity activity) {
        LauncherFragment routerFragment = findFragment(activity);
        if (routerFragment == null) {
            routerFragment = LauncherFragment.newInstance();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(routerFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return routerFragment;
    }

    private LauncherFragment findFragment(FragmentActivity activity) {
        return (LauncherFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    public void startActivityForResult(Class<?> clazz, Callback callback) {
        Intent intent = new Intent(mContext, clazz);
        startActivityForResult(intent, callback);
    }

    public void startActivityForResult(Intent intent, Callback callback) {
        mFragment.startActivityForResult(intent, callback);
    }

}
