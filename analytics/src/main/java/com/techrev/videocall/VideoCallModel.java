package com.techrev.videocall;

import com.twilio.video.CameraCapturer;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalDataTrack;
import com.twilio.video.LocalDataTrackPublication;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;

public class VideoCallModel {
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private LocalDataTrack localDataTrack;
    private LocalParticipant localParticipant;
    private CameraCapturer.CameraSource cameraSource;

    public boolean isConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(boolean connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    private boolean connectionStatus=false;
    private boolean localAudioStatus=true;

    public boolean isLocalAudioStatus() {
        return localAudioStatus;
    }

    public void setLocalAudioStatus(boolean localAudioStatus) {
        this.localAudioStatus = localAudioStatus;
    }

    public boolean isLocalVideoStatus() {
        return localVideoStatus;
    }

    public void setLocalVideoStatus(boolean localVideoStatus) {
        this.localVideoStatus = localVideoStatus;
    }

    private boolean localVideoStatus=true;

    LocalDataTrackPublication localDataTrackPublicationGlobal;
    private CameraCapturerCompat cameraCapturerCompat;

    public CameraCapturerCompat getCameraCapturerCompat() {
        return cameraCapturerCompat;
    }

    public void setCameraCapturerCompat(CameraCapturerCompat cameraCapturerCompat) {
        this.cameraCapturerCompat = cameraCapturerCompat;
    }

    public LocalAudioTrack getLocalAudioTrack() {
        return localAudioTrack;
    }

    public void setLocalAudioTrack(LocalAudioTrack localAudioTrack) {
        this.localAudioTrack = localAudioTrack;
    }

    public LocalVideoTrack getLocalVideoTrack() {
        return localVideoTrack;
    }

    public void setLocalVideoTrack(LocalVideoTrack localVideoTrack) {
        this.localVideoTrack = localVideoTrack;
    }

    public LocalDataTrack getLocalDataTrack() {
        return localDataTrack;
    }

    public void setLocalDataTrack(LocalDataTrack localDataTrack) {
        this.localDataTrack = localDataTrack;
    }

    public LocalParticipant getLocalParticipant() {
        return localParticipant;
    }

    public void setLocalParticipant(LocalParticipant localParticipant) {
        this.localParticipant = localParticipant;
    }

    public LocalDataTrackPublication getLocalDataTrackPublicationGlobal() {
        return localDataTrackPublicationGlobal;
    }

    public void setLocalDataTrackPublicationGlobal(LocalDataTrackPublication localDataTrackPublicationGlobal) {
        this.localDataTrackPublicationGlobal = localDataTrackPublicationGlobal;
    }


    public void publishAllTrack()
    {
        this.localParticipant.publishTrack(this.localDataTrack);
        this.localParticipant.publishTrack(this.localAudioTrack);
        this.localParticipant.publishTrack(this.localVideoTrack);
    }
    public void releaseAllTracks()
    {
        if (localAudioTrack != null) {
            localAudioTrack.release();
            localAudioTrack = null;
        }
        if (localVideoTrack != null) {
            localVideoTrack.release();
            localVideoTrack = null;
        }
        if (localDataTrack != null) {
            localDataTrack.release();
            localDataTrack = null;
        }
    }

    public CameraCapturer.CameraSource getCameraSource() {
        return cameraSource;
    }

    public void setCameraSource(CameraCapturer.CameraSource cameraSource) {
        this.cameraSource = cameraSource;
    }

    public void releaseVideoTrack(){
        if (localVideoTrack != null) {
            localVideoTrack.release();
            localVideoTrack = null;
        }
    }

}
