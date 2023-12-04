package com.techrev.analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CosignerVerificationModel {
    @Expose
    @SerializedName("results")
    private ArrayList<VerificationData> results;

    @Expose
    @SerializedName("total")
    private int total;

    @Expose
    @SerializedName("pageNumber")
    private int pageNumber;

    public ArrayList<VerificationData> getResults() {
        return results;
    }

    public void setResults(ArrayList<VerificationData> results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }


    public class VerificationData {

        @Expose
        @SerializedName("userId")
        private int userId;

        @Expose
        @SerializedName("user_access_code")
        private String user_access_code;

        @Expose
        @SerializedName("IDMSessionId")
        private String IDMSessionId;

        @Expose
        @SerializedName("documentIdVerificationAttemptCount")
        private int documentIdVerificationAttemptCount;

        @Expose
        @SerializedName("kbaAttemptCount")
        private int kbaAttemptCount;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUser_access_code() {
            return user_access_code;
        }

        public void setUser_access_code(String user_access_code) {
            this.user_access_code = user_access_code;
        }

        public String getIDMSessionId() {
            return IDMSessionId;
        }

        public void setIDMSessionId(String IDMSessionId) {
            this.IDMSessionId = IDMSessionId;
        }

        public int getDocumentIdVerificationAttemptCount() {
            return documentIdVerificationAttemptCount;
        }

        public void setDocumentIdVerificationAttemptCount(int documentIdVerificationAttemptCount) {
            this.documentIdVerificationAttemptCount = documentIdVerificationAttemptCount;
        }

        public int getKbaAttemptCount() {
            return kbaAttemptCount;
        }

        public void setKbaAttemptCount(int kbaAttemptCount) {
            this.kbaAttemptCount = kbaAttemptCount;
        }

        @Expose
        @SerializedName("isKbaVerified")
        private int isKbaVerified;

        public int isKbaVerified() {
            return isKbaVerified;
        }

        public void setKbaVerified(int kbaVerified) {
            isKbaVerified = kbaVerified;
        }

        public int isDocumentVerified() {
            return isDocumentVerified;
        }

        public void setDocumentVerified(int documentVerified) {
            isDocumentVerified = documentVerified;
        }

        @Expose
        @SerializedName("isDocumentVerified")
        private int isDocumentVerified;
    }

}
