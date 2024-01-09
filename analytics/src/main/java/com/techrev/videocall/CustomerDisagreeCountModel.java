package com.techrev.videocall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CustomerDisagreeCountModel {

    @SerializedName("results")
    @Expose
    private ArrayList<DisagreeCount> disagreeCounts;

    public ArrayList<DisagreeCount> getDisagreeCounts() {
        return disagreeCounts;
    }

    public void setDisagreeCounts(ArrayList<DisagreeCount> disagreeCounts) {
        this.disagreeCounts = disagreeCounts;
    }

    public class DisagreeCount {

        @Expose
        @SerializedName("RequestId")
        private String RequestId;

        @Expose
        @SerializedName("customerDisagreeCount")
        private String customerDisagreeCount;

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String requestId) {
            RequestId = requestId;
        }

        public String getCustomerDisagreeCount() {
            return customerDisagreeCount;
        }

        public void setCustomerDisagreeCount(String customerDisagreeCount) {
            this.customerDisagreeCount = customerDisagreeCount;
        }
    }
}
