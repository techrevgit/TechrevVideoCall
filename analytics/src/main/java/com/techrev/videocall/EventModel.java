package com.techrev.videocall;

import com.twilio.video.RemoteVideoTrack;

import java.util.ArrayList;

public class EventModel {
    private VideoCallModel videoCallModel;
    RemoteVideoTrack remoteVideoTrack;

    public RemoteVideoTrack getRemoteVideoTrack() {
        return remoteVideoTrack;
    }

    public void setRemoteVideoTrack(RemoteVideoTrack remoteVideoTrack) {
        this.remoteVideoTrack = remoteVideoTrack;
    }

    private DataModel dataModel;
    private ArrayList<TechrevRemoteParticipant> remoteParticipantList = new ArrayList<TechrevRemoteParticipant>();

    public DataModel getBankerdModel() {
        return bankerdModel;
    }

    public void setBankerdModel(DataModel bankerdModel) {
        this.bankerdModel = bankerdModel;
    }

    DataModel bankerdModel;
    public ArrayList<TechrevRemoteParticipant> getRemoteParticipantList() {
        return remoteParticipantList;
    }

    public void setRemoteParticipantList(ArrayList<TechrevRemoteParticipant> remoteParticipantList) {

        if(this.remoteParticipantList!=null)
        {
            this.remoteParticipantList.clear();
        }
        this.remoteParticipantList.addAll(remoteParticipantList);

    }

    public TechrevRemoteParticipant getTechrevRemoteParticipant() {
        return techrevRemoteParticipant;
    }

    public void setTechrevRemoteParticipant(TechrevRemoteParticipant techrevRemoteParticipant) {
        this.techrevRemoteParticipant = techrevRemoteParticipant;
    }

    private TechrevRemoteParticipant techrevRemoteParticipant;

    public int getEventType() {
        return EventType;
    }

    public EventModel(VideoCallModel videoCallModel, DataModel dataModel, int eventType) {
        this.videoCallModel = videoCallModel;
        this.dataModel = dataModel;
        EventType = eventType;
    }

    public VideoCallModel getVideoCallModel() {
        return videoCallModel;
    }

    public void setVideoCallModel(VideoCallModel videoCallModel) {
        this.videoCallModel = videoCallModel;
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setEventType(int eventType) {
        EventType = eventType;
    }

    private int EventType;
}
