package com.mmednet.library.http.okhttp;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Title:OkHttpTrustAllCerts
 * <p>
 * Description:HTTPS
 * </p>
 * Author Jming.L
 * Date 2019/3/22 18:41
 */
public class OkHttpTrustAllCerts implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}
