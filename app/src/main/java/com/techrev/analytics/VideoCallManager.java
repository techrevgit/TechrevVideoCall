package com.techrev.analytics;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;


@TargetApi(29)
public class VideoCallManager {
    private VideoCallService mService;
    private Context mContext;
    private State currentState = State.UNBIND_SERVICE;
    private String token=null;
    private String roomName=null;
    private String userMeetingIdentifier=null;
    private Intent intent;
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to ScreenCapturerService, cast the IBinder and get ScreenCapturerService instance
            VideoCallService.LocalBinder binder = (VideoCallService.LocalBinder) service;
            mService = binder.getService();
            currentState = State.BIND_SERVICE;
            intent = new Intent(mContext, VideoCallService.class);
            intent.putExtra("token",token);
            intent.putExtra("room",roomName);
            intent.putExtra("userMeetingIdentifier",userMeetingIdentifier);
            mContext.startService(intent);


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            Log.d("xx","XX");
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

    VideoCallManager(Context context,String token,String room,String userMeetingIdentifier) {
        mContext = context;
        bindService(token,room,userMeetingIdentifier);
    }

    private void bindService(String token,String room,String userMeetingIdentifier) {
        Intent intent = new Intent(mContext, VideoCallService.class);
        this.token=token;
        this.roomName=room;
        this.userMeetingIdentifier=userMeetingIdentifier;
        intent.putExtra("token",token);
        intent.putExtra("room",room);
        intent.putExtra("userMeetingIdentifier",userMeetingIdentifier);
        mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void startForeground() {

        currentState = State.START_FOREGROUND;
    }

    public void appInForeground(boolean status) {
        if( currentState == State.BIND_SERVICE)
        {
            mService.appInForeground(status);
        }

    }

    public void appInBackground() {
        if(mService != null)
        mService.appInBackground();

    }

    public void setLocalAudioStatusToService(boolean status) {
        mService.setLocalAudioStatusToService(status);

    }

    public void setLocalVideoStatusToService(boolean status) {
        mService.setLocalVideoStatusToService(status);

    }

    public void endForeground() {
        mService.endForeground();
        currentState = State.END_FOREGROUND;
    }

    public void unbindService() {
        mContext.stopService(intent);
        mContext.unbindService(connection);
        currentState = State.UNBIND_SERVICE;
    }

    /*Added By Rupesh*/
    public void resumeVideoTracks(){
        if(mService != null)
            mService.resumeVideoTracks();
    }
    public void resumeVideoTrackPublish(){
        if(mService != null)
            mService.resumeVideoTrackPublish();
    }
    /*Added By Rupesh*/

}