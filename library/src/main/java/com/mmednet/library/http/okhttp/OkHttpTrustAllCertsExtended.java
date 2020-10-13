package com.mmednet.library.http.okhttp;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;

/**
 * Title:OkHttpTrustAllCerts
 * <p>
 * Description:HTTPS
 * </p>
 * Author Jming.L
 * Date 2019/3/22 18:41
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class OkHttpTrustAllCertsExtended extends X509ExtendedTrustManager {


    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {

    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

    }

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
