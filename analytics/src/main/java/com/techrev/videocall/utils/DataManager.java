package com.techrev.videocall.utils;

import com.techrev.videocall.models.DataModel;
import com.techrev.videocall.models.VideoCallModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rupesh Kumar Jena on 16-10-2024.
 */
public class DataManager {

    private static DataManager instance;
    private List<DataModel> dataModelList = new ArrayList<>();
    private VideoCallModel videoCallModel;

    private DataManager() {
        // private constructor to enforce Singleton pattern
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public List<DataModel> getDataModelList() {
        return dataModelList;
    }

    public void setDataModelList(List<DataModel> dataModelList) {
        this.dataModelList = dataModelList;
    }

    public VideoCallModel getVideoCallModel() {
        return videoCallModel;
    }

    public void setVideoCallModel(VideoCallModel videoCallModel) {
        this.videoCallModel = videoCallModel;
    }

}
