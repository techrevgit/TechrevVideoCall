package com.techrev.videocall.ui.mydocuments;

import okhttp3.RequestBody;

public class UploadRequesterDocumentRequestModel {

    private String userType;
    private String RequestId;
    private RequestBody documentName;
    private String userId;
    private Boolean isTempRequest;
    private String dateTime;
    private String tempRequestId;
    private Boolean isDewDoc;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public RequestBody getDocumentName() {
        return documentName;
    }

    public void setDocumentName(RequestBody documentName) {
        this.documentName = documentName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsTempRequest() {
        return isTempRequest;
    }

    public void setIsTempRequest(Boolean isTempRequest) {
        this.isTempRequest = isTempRequest;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTempRequestId() {
        return tempRequestId;
    }

    public void setTempRequestId(String tempRequestId) {
        this.tempRequestId = tempRequestId;
    }

    public Boolean getIsDewDoc() {
        return isDewDoc;
    }

    public void setIsDewDoc(Boolean isDewDoc) {
        this.isDewDoc = isDewDoc;
    }

    public UploadRequesterDocumentRequestModel (String userType, String RequestId, RequestBody documentName,
                                                String userId, Boolean isTempRequest, String dateTime, String tempRequestId,
                                                Boolean isDewDoc) {
        this.userType = userType;
        this.RequestId = RequestId;
        this.documentName = documentName;
        this.userId = userId;
        this.isTempRequest = isTempRequest;
        this.dateTime = dateTime;
        this.tempRequestId = tempRequestId;
        this.isDewDoc = isDewDoc;

    }

}
