package com.techrev.videocall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttachedFileUploadResponseModel {

    @Expose
    @SerializedName("remoteFileName")
    private String docId;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
