package com.techrev.videocall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadImageModel {
    @SerializedName("requestId")
    @Expose
    private String requestId;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("uploadedDocId")
    @Expose
    private String uploadedDocId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUploadedDocId() {
        return uploadedDocId;
    }

    public void setUploadedDocId(String uploadedDocId) {
        this.uploadedDocId = uploadedDocId;
    }
}
