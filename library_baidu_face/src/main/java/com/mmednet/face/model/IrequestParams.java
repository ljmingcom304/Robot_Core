/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.mmednet.face.model;

import java.io.File;
import java.util.Map;

public interface IrequestParams {

    Map<String, File> getFileParams();
    Map<String, String> getStringParams();
    String getJsonParams();
}
