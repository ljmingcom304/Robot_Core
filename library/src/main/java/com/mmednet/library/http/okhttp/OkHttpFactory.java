package com.mmednet.library.http.okhttp;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Title:OkHttpFactory
 * <p>
 * Description:HTTPS
 * </p>
 * Author Jming.L
 * Date 2019/3/22 18:42
 */
public class OkHttpFactory {

    public static SSLSocketFactory buildSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new OkHttpTrustAllCerts()}, new SecureRandom());
            ssfFactory = context.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

}
