package com.techrev.videocall.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestDocModel {

    @Expose
    @SerializedName("results")
    private List<RequestDocuments> requestDocuments;

    public List<RequestDocuments> getRequestDocuments() {
        return requestDocuments;
    }

    public void setRequestDocuments(List<RequestDocuments> requestDocuments) {
        this.requestDocuments = requestDocuments;
    }

    public class RequestDocuments {

        @Expose
        @SerializedName("reqDocId")
        private String reqDocId;

        @Expose
        @SerializedName("requestId")
        private String requestId;

        @Expose
        @SerializedName("uploadedDocId")
        private String uploadedDocId;

        @Expose
        @SerializedName("UploadedDateTime")
        private String UploadedDateTime;

        @Expose
        @SerializedName("originalUploadedDocId")
        private String originalUploadedDocId;

        @Expose
        @SerializedName("documentName")
        private String documentName;

        @Expose
        @SerializedName("DocId")
        private String DocId;

        @Expose
        @SerializedName("requestMasterType")
        private String requestMasterType;

        public String getReqDocId() {
            return reqDocId;
        }

        public void setReqDocId(String reqDocId) {
            this.reqDocId = reqDocId;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getUploadedDocId() {
            return uploadedDocId;
        }

        public void setUploadedDocId(String uploadedDocId) {
            this.uploadedDocId = uploadedDocId;
        }

        public String getUploadedDateTime() {
            return UploadedDateTime;
        }

        public void setUploadedDateTime(String uploadedDateTime) {
            UploadedDateTime = uploadedDateTime;
        }

        public String getOriginalUploadedDocId() {
            return originalUploadedDocId;
        }

        public void setOriginalUploadedDocId(String originalUploadedDocId) {
            this.originalUploadedDocId = originalUploadedDocId;
        }

        public String getDocumentName() {
            return documentName;
        }

        public void setDocumentName(String documentName) {
            this.documentName = documentName;
        }

        public String getDocId() {
            return DocId;
        }

        public void setDocId(String docId) {
            DocId = docId;
        }

        public String getRequestMasterType() {
            return requestMasterType;
        }

        public void setRequestMasterType(String requestMasterType) {
            this.requestMasterType = requestMasterType;
        }

    }
}
