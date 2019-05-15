/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.mmednet.face.parser;


import com.mmednet.face.exception.FaceError;

/**
 * JSON解析
 * @param <T>
 */
public interface Iparser<T> {
    T parse(String json) throws FaceError;
}
