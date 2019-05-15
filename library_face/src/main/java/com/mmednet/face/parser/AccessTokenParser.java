/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.mmednet.face.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.mmednet.face.exception.FaceError;
import com.mmednet.face.bean.AccessToken;
import com.mmednet.face.parser.Iparser;
import com.mmednet.face.utils.LogUtil;

public class AccessTokenParser implements Iparser<AccessToken> {
    @Override
    public AccessToken parse(String json) throws FaceError {
        try {
            AccessToken accessToken = new AccessToken();
            accessToken.setJson(json);
            JSONObject jsonObject = new JSONObject(json);
            LogUtil.i("ORC", "json token->" + json);

            if (jsonObject != null) {

                accessToken.setAccessToken(jsonObject.optString("access_token"));
                accessToken.setExpiresIn(jsonObject.optInt("expires_in"));
                return accessToken;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            FaceError error = new FaceError(FaceError.ErrorCode.JSON_PARSE_ERROR, "Json parse error", e);
            throw error;
        }
        return null;
    }
}
