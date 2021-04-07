package com.mmednet.apollo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.Subscription;
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport;
import com.mmednet.apollo.okhttp.OkCookie;
import com.mmednet.apollo.okhttp.OkHttpFactory;
import com.mmednet.apollo.okhttp.OkHttpTrustAllCerts;
import com.mmednet.apollo.okhttp.OkHttpTrustAllCertsExtended;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Title:Graphql
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2021/1/18 11:21
 */
public class Graphql {

    private final ApolloClient mApolloClient;
    private final OkHttpClient.Builder mClientBuilder;
    private static Application mApplication;
    private static final String HTTP = "Graphql_Http";
    private static final String SOCKET = "Graphql_WebSocket";

    private Graphql(String token) {
        mClientBuilder = new OkHttpClient.Builder();
        mClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        mClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        mClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        if (token != null) {
            mClientBuilder.addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                    String credential = "Bearer " + token;
                    Request request = chain.request().newBuilder().addHeader("Authorization", credential).build();
                    return chain.proceed(request);
                }
            });
        } else {
            Log.e(Graphql.class.getSimpleName(), "GraphqlToken is null");
        }
        mClientBuilder.cookieJar(new OkCookie());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            OkHttpTrustAllCertsExtended certsExtended = new OkHttpTrustAllCertsExtended();
            mClientBuilder.sslSocketFactory(OkHttpFactory.buildSSLSocketFactory(certsExtended), certsExtended);
        } else {
            mClientBuilder.sslSocketFactory(OkHttpFactory.buildSSLSocketFactory(new OkHttpTrustAllCerts()));
        }
        mClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @SuppressLint("BadHostnameVerifier")
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mApplication);
        String httpAddress = preferences.getString(HTTP, "https://api-expert-mmed.ebilin.net:8443/graphql");
        String webSocketAddress = preferences.getString(SOCKET, "wss://api-expert-mmed.ebilin.net:8443/graphql");
        mApolloClient = ApolloClient.builder()
                .serverUrl(httpAddress)
                .okHttpClient(mClientBuilder.build())
                .subscriptionTransportFactory(new WebSocketSubscriptionTransport.Factory(
                        webSocketAddress, mClientBuilder.build()))
                .build();
    }


    public ApolloClient getApolloClient() {
        return mApolloClient;
    }


    public static <D extends Query.Data, T, V extends Query.Variables> void query(
            @NotNull Query<D, T, V> query, @Nullable Callback<T> callback) {
        query(null, query, callback);
    }

    public static <D extends Query.Data, T, V extends Query.Variables> void query(
            String token, @NotNull Query<D, T, V> query, @Nullable Callback<T> callback) {
        new Graphql(token).getApolloClient().query(query).enqueue(callback);
    }

    public static <D extends Query.Data, T, V extends Query.Variables> void mutation(
            @NotNull Mutation<D, T, V> mutation, @Nullable Callback<T> callback) {
        mutation(null, mutation, callback);
    }

    public static <D extends Query.Data, T, V extends Query.Variables> void mutation(
            String token, @NotNull Mutation<D, T, V> mutation, @Nullable Callback<T> callback) {
        if (token == null) {
            Log.e(Graphql.class.getSimpleName(), "GraphqlToken is null");
            return;
        }
        new Graphql(token).getApolloClient().mutate(mutation).enqueue(callback);
    }

    public static <D extends Subscription.Data, T, V extends Subscription.Variables> void subscribe(
            Subscription<D, T, V> subscription, Callback<T> callback) {
        subscribe(null, subscription, callback);
    }

    public static <D extends Subscription.Data, T, V extends Subscription.Variables> void subscribe(
            String token, Subscription<D, T, V> subscription, Callback<T> callback) {
        new Graphql(token).getApolloClient().subscribe(subscription).execute(callback);
    }

    public static void initAddress(Application application, String http, String webSocket) {
        mApplication = application;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HTTP, http);
        editor.putString(SOCKET, webSocket);
        editor.apply();
    }

    public static void login() {
        LoginUserMutation mutation = LoginUserMutation.builder()
                .username("medapp").password("Passw0rd@21").build();
        Graphql.mutation(mutation, new Callback<LoginUserMutation.Data>() {
            @Override
            public void onSuccess(@NotNull Response<LoginUserMutation.Data> response) {
                LoginUserMutation.Data data = response.getData();
                if (data != null) {

                }
            }
        });
    }


}
