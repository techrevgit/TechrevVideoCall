package com.techrev.videocall.ui.videocallroom;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.techrev.videocall.services.ScreenCapturerService;

@TargetApi(29)
public class ScreenCapturerManager {
    private ScreenCapturerService mService;
    private Context mContext;
    private State currentState = State.UNBIND_SERVICE;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to ScreenCapturerService, cast the IBinder and get ScreenCapturerService instance
            ScreenCapturerService.LocalBinder binder = (ScreenCapturerService.LocalBinder) service;
            mService = binder.getService();
            currentState = State.BIND_SERVICE;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    /**
     * An enum describing the possible states of a ScreenCapturerManager.
     */
    public enum State {
        BIND_SERVICE,
        START_FOREGROUND,
        END_FOREGROUND,
        UNBIND_SERVICE
    }

    ScreenCapturerManager(Context context) {
        mContext = context;
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent(mContext, ScreenCapturerService.class);
        mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void startForeground() {
        mService.startForeground();
        currentState = State.START_FOREGROUND;
    }

    public void endForeground() {
        mService.endForeground();
        currentState = State.END_FOREGROUND;
    }

    public void unbindService() {
        mContext.unbindService(connection);
        currentState = State.UNBIND_SERVICE;
    }
}