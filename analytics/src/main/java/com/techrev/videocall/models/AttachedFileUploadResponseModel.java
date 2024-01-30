package com.techrev.videocall.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttachedFileUploadResponseModel {

    @Expose
    @SerializedName("success")
    private String success;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("docId")
    private String docId;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
