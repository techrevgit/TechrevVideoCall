package com.techrev.videocall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignerSignatureInitialAuthorizationModel {

    @Expose
    @SerializedName("results")
    private AuthorizationDetails authorizationDetails;

    public AuthorizationDetails getAuthorizationDetails() {
        return authorizationDetails;
    }

    public void setAuthorizationDetails(AuthorizationDetails authorizationDetails) {
        this.authorizationDetails = authorizationDetails;
    }

    public class AuthorizationDetails {

        @Expose
        @SerializedName("requestParticipantId")
        private String requestParticipantId;
        @Expose
        @SerializedName("requestId")
        private String requestId;
        @Expose
        @SerializedName("userId")
        private String userId;
        @Expose
        @SerializedName("isPrimarySigner")
        private String isPrimarySigner;
        @Expose
        @SerializedName("hasAuthorizedForSignature")
        private String hasAuthorizedForSignature;
        @Expose
        @SerializedName("hasAuthorizedForInitial")
        private String hasAuthorizedForInitial;

        public String getRequestParticipantId() {
            return requestParticipantId;
        }

        public void setRequestParticipantId(String requestParticipantId) {
            this.requestParticipantId = requestParticipantId;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getIsPrimarySigner() {
            return isPrimarySigner;
        }

        public void setIsPrimarySigner(String isPrimarySigner) {
            this.isPrimarySigner = isPrimarySigner;
        }

        public String getHasAuthorizedForSignature() {
            return hasAuthorizedForSignature;
        }

        public void setHasAuthorizedForSignature(String hasAuthorizedForSignature) {
            this.hasAuthorizedForSignature = hasAuthorizedForSignature;
        }

        public String getHasAuthorizedForInitial() {
            return hasAuthorizedForInitial;
        }

        public void setHasAuthorizedForInitial(String hasAuthorizedForInitial) {
            this.hasAuthorizedForInitial = hasAuthorizedForInitial;
        }

    }

}
