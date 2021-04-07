package com.mmednet.apollo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title:Callback
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2021/2/3 13:56
 */
public abstract class Callback<T> extends ApolloCall.Callback<T> implements ApolloSubscriptionCall.Callback<T> {

    private static final String TAG = Callback.class.getSimpleName();

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                @SuppressWarnings("unchecked")
                Response<T> response = (Response<T>) msg.obj;
                onSuccess(response);
            } else {
                String error = (String) msg.obj;
                onFailure(error);
            }
        }
    };

    @Override
    public final void onResponse(@NotNull Response<T> response) {
        Message message = mHandler.obtainMessage();
        if (response.hasErrors()) {
            List<Error> errors = response.getErrors();
            String errorMessage = "";
            for (int i = 0; errors != null && i < errors.size(); i++) {
                //{statusCode=401, error=Unauthorized, message=Invalid username or password}
                Error error = errors.get(i);
                Map<String, Object> hashMap = error.getCustomAttributes();
                if (i == 0) {
                    errorMessage = error.getMessage();
                }
                Log.e(TAG, "Graphql[Error][" + i + "]:" + error.getMessage());
                for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                    Log.e(TAG, "Graphql[Error][" + i + "]:" + entry.getKey() + ":" + entry.getValue());
                }
            }
            Log.i(TAG, "Graphql[Error]:" + response.toString());
            message.what = 1;
            message.obj = errorMessage;
            mHandler.sendMessage(message);
        } else {
            Log.i(TAG, "Graphql[Success]:" + response.toString());
            message.what = 0;
            message.obj = response;
            mHandler.sendMessage(message);
        }
    }

    public abstract void onSuccess(@NotNull Response<T> response);

    public void onFailure(String message) {

    }

    @Override
    public final void onFailure(@NotNull ApolloException ex) {
        Log.e(TAG, "Graphql[Failure]:" + ex.getMessage(), ex);
        Message message = mHandler.obtainMessage();
        message.what = 1;
        message.obj = ex.getMessage();
        mHandler.sendMessage(message);
    }

    @Override
    public void onCompleted() {
        Log.e(TAG, "Graphql[Completed]");
    }

    @Override
    public void onTerminated() {
        Log.e(TAG, "Graphql[Terminated]");
    }

    @Override
    public void onConnected() {
        Log.e(TAG, "Graphql[Connected]");
    }


}
