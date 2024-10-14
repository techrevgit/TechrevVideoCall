package com.techrev.videocall.ui.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.techrev.videocall.ui.internet.NoInternetActivity;


public class BaseApplication extends Application {

    private ConnectivityManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.registerDefaultNetworkCallback(networkCallback);
        }
        if (!isInternetAvailable()) {
            //openNoInternetScreen();
        }
    }

    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            // this ternary operation is not quite true, because non-metered doesn't yet mean, that it's wifi
            // nevertheless, for simplicity let's assume that's true
            if (NoInternetActivity.activity != null) {
                NoInternetActivity.activity.finish();
            }
            Log.i("vvv", "connected to " + (manager.isActiveNetworkMetered() ? "LTE" : "WIFI"));
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            Log.i("vvv", "losing active connection");
            //openNoInternetScreen();
        }
    };

    private void openNoInternetScreen() {
        Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean isInternetAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }


    }


}
