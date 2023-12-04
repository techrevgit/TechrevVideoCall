package com.techrev.analytics;

import android.util.Log;

import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrackPublication;

public class TechrevRemoteParticipant {
    boolean techRevAudioEnable;
    boolean techRevVideoEnable;

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    boolean isSpeaking;

    public boolean isTechRevScreenEnable() {
        return techRevScreenEnable;
    }

    public void setTechRevScreenEnable(boolean techRevScreenEnable) {
        this.techRevScreenEnable = techRevScreenEnable;
    }

    boolean techRevScreenEnable;

    public boolean isTechRevScreenSelected() {
        return techRevScreenSelected;
    }

    public void setTechRevScreenSelected(boolean techRevScreenSelected) {
        this.techRevScreenSelected = techRevScreenSelected;
    }

    boolean techRevScreenSelected;

    public boolean isTechRevRemoveMyself() {
        return techRevRemoveMyself;
    }

    public void setTechRevRemoveMyself(boolean techRevRemoveMyself) {
        this.techRevRemoveMyself = techRevRemoveMyself;
    }

    boolean techRevRemoveMyself;

    public RemoteParticipant getRemoteParticipant() {
        return remoteParticipant;
    }

    public void setRemoteParticipant(RemoteParticipant remoteParticipant) {
        this.remoteParticipant = remoteParticipant;
    }

    RemoteParticipant remoteParticipant;


    void TechrevRemoteParticipant() {

    }

    public TechrevRemoteParticipant checkAudioVideo(RemoteParticipant remoteParticipant1) {

        remoteParticipant = remoteParticipant1;
        if (remoteParticipant != null) {
            Log.d("====remoteParticipant", "IF Not null");
            // Added for Audio Mute and UnMute
            if (remoteParticipant.getRemoteAudioTracks().size() > 0) {
                RemoteAudioTrackPublication remoteAudioTrackPublication = remoteParticipant.getRemoteAudioTracks().get(0);
                if (remoteAudioTrackPublication.isTrackEnabled() || remoteAudioTrackPublication.isTrackSubscribed()) {
                    Log.d("===setAudioEnable", "True");
                    this.setTechRevAudioEnable(true);

                } else {
                    Log.d("===setAudioEnable", "False");
                    this.setTechRevAudioEnable(false);
                }

            }
            // Added for Video On and Off
            if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
                RemoteVideoTrackPublication remoteVideoTrackPublication = remoteParticipant.getRemoteVideoTracks().get(0);
                if (remoteVideoTrackPublication.isTrackEnabled() || remoteVideoTrackPublication.isTrackSubscribed()) {
                    this.setTechRevVideoEnable(true);
                } else {
                    this.setTechRevVideoEnable(false);
                }
            }

        } else {
            Log.d("====remoteParticipant", "ELSE null");
        }

        return this;

    }

    public boolean getTechRevAudioEnable() {
        return techRevAudioEnable;
    }

    public void setTechRevAudioEnable(boolean techRevAudioEnable) {
        this.techRevAudioEnable = techRevAudioEnable;
    }

    public boolean getTechRevVideoEnable() {
        return techRevVideoEnable;
    }

    public void setTechRevVideoEnable(boolean techRevVideoEnable) {
        this.techRevVideoEnable = techRevVideoEnable;
    }
}
