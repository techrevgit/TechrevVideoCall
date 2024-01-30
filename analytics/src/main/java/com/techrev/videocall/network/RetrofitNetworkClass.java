//package .TechRevVideoConferenceWrapper;
package com.techrev.videocall.network;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.techrev.videocall.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitNetworkClass {

    private static final String TAG = "RetrofitNetworkClass";
    public static Retrofit networkCall;
    private static boolean isInitialized = false;
    private static final Object lock = new Object();
    //For Live Production
//    public static final String BASE_URL = "https://app.eNotary.net/api/";
    //String BASE_URL_VAL = "https://apias.digitalnotarize.com/api/";
    // For Testing
    String BASE_URL_VAL = Constants.API_BASE_URL + "api/";
    //String BUSINESS_API_URL_VAL = "https://businessdevapi.digitalnotarize.com/api/";
    String BUSINESS_API_URL_VAL = "https://businessapi.digitalnotarize.com/api/";

    // Basic Retrofit Calling
    public Retrofit callingURL() {
        return createRetrofitInBackground(BASE_URL_VAL);
    }

    public Retrofit businessApiCallingURL() {
        return createRetrofitInBackground(BUSINESS_API_URL_VAL);
    }

    private Retrofit createRetrofitInBackground(String baseUrl) {
        synchronized (lock) {
            if (isInitialized) {
                return networkCall;
            }

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(logging)
                    .build();

            networkCall = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            isInitialized = true;
            return networkCall;
        }
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
