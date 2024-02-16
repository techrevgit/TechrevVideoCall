package com.techrev.videocall.ui.mydocuments;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RequesterDocumentModel {

    @SerializedName("requestId")
    private String requestId;

    @SerializedName("DocIds")
    private List<DocIdObject> docIds;

    public RequesterDocumentModel() {

    }

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

    public JSONObject buildRequestObject(List<String> docIds, String requestId) {
        JSONObject requestObject = new JSONObject();
        JSONArray docIdsArray = new JSONArray();

        try {
            // Constructing the array of DocId objects
            for (String docId : docIds) {
                JSONObject docIdObject = new JSONObject();
                docIdObject.put("DocId", docId);
                docIdsArray.put(docIdObject);
            }

            // Adding the array and requestId to the main request object
            requestObject.put("DocIds", docIdsArray);
            requestObject.put("requestId", requestId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestObject;
    }

}
