package com.techrev.videocall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadFileModel
{
    @SerializedName("mimeType")
    @Expose
    private String mimeType;

    @SerializedName("originalFileName")
    @Expose
    private String originalFileName;

    @SerializedName("remoteFileName")
    @Expose
    private String remoteFileName;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }

}
