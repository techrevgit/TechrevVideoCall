//package .TechRevVideoConferenceWrapper;
package com.techrev.videocall.network;

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
            /*if (isInitialized) {
                return networkCall;
            }*/

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

}
