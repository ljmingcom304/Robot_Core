/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.mmednet.face.utils;


import com.mmednet.face.exception.FaceError;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceError error);
}
