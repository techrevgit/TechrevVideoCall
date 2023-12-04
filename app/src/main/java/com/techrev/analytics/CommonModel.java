package com.techrev.analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonModel{

    @Expose
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
