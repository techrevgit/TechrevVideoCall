package com.techrev.videocall.ui.mydocuments;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequesterDocumentModel {

    @SerializedName("requestId")
    private String requestId;

    @SerializedName("DocIds")
    private List<DocIdObject> docIds;

    public RequesterDocumentModel(String requestId, List<DocIdObject> docIds) {
        this.requestId = requestId;
        this.docIds = docIds;
    }

    public static class DocIdObject {
        @SerializedName("DocId")
        private String docId;

        public DocIdObject(String docId) {
            this.docId = docId;
        }
    }

}
