//package cordova_plugin_techrev_videoconference.TechRevVideoConferenceWrapper;
package com.techrev.analytics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitNetworkClass {

    public static Retrofit networkCall;
    //For Live Production
//    public static final String BASE_URL = "https://app.eNotary.net/api/";
    //String BASE_URL_VAL = "https://apias.digitalnotarize.com/api/";
    // For Testing
    String BASE_URL_VAL = Constants.API_BASE_URL + "api/";
    String BUSINESS_API_URL_VAL = "https://businessapi.digitalnotarize.com/api/";

    // Basic Retrofit Calling
    public Retrofit callingURL() {
        //Start
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                /*.addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                *//*.header("Cache-Control", "public, only-if-cached," +
                                        "max-stale=" + 60 * 60 * 24)*//*
                                .build();

                        return chain.proceed(request);
                    }
                })*/
                .addInterceptor(logging)
                .build();
        networkCall = new Retrofit.Builder()
                .baseUrl(BASE_URL_VAL)
                .client(okClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return networkCall;
    }

    public Retrofit businessApiCallingURL() {
        //Start
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                /*.addInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                *//*.header("Cache-Control", "public, only-if-cached," +
                                        "max-stale=" + 60 * 60 * 24)*//*
                                .build();

                        return chain.proceed(request);
                    }
                })*/
                .addInterceptor(logging)
                .build();
        networkCall = new Retrofit.Builder()
                .baseUrl(BUSINESS_API_URL_VAL)
                .client(okClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return networkCall;
    }


    // To check Online Connection
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        NetworkInfo typemo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo tywi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        NetworkInfo tywifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (netInfo != null && netInfo.isConnectedOrConnecting()
                || typemo != null && typemo.isConnectedOrConnecting()
                || tywi != null && tywi.isConnectedOrConnecting()
                || tywifi != null && tywifi.isConnectedOrConnecting())
        {
            return true;
        } else {
            return false;
        }
    }

}
