package com.techrev.videocall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyAllDocListModel {

    @Expose
    @SerializedName("results")
    private List<AllDocs> allDocs;

    public List<AllDocs> getAllDocs() {
        return allDocs;
    }

    public void setAllDocs(List<AllDocs> allDocs) {
        this.allDocs = allDocs;
    }

    public class AllDocs {

        @Expose
        @SerializedName("DocumentName")
        private String DocumentName;

        @Expose
        @SerializedName("DocId")
        private String DocId;

        @Expose
        @SerializedName("UpdatedDateTime")
        private String UpdatedDateTime;

        @Expose
        @SerializedName("DocType")
        private String DocType;

        @Expose
        @SerializedName("parentDocId")
        private String parentDocId;

        @Expose
        @SerializedName("status")
        private String status;

        @Expose
        @SerializedName("message")
        private String message;

        public String getDocumentName() {
            return DocumentName;
        }

        public void setDocumentName(String documentName) {
            DocumentName = documentName;
        }

        public String getDocId() {
            return DocId;
        }

        public void setDocId(String docId) {
            DocId = docId;
        }

        public String getUpdatedDateTime() {
            return UpdatedDateTime;
        }

        public void setUpdatedDateTime(String updatedDateTime) {
            UpdatedDateTime = updatedDateTime;
        }

        public String getDocType() {
            return DocType;
        }

        public void setDocType(String docType) {
            DocType = docType;
        }

        public String getParentDocId() {
            return parentDocId;
        }

        public void setParentDocId(String parentDocId) {
            this.parentDocId = parentDocId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
