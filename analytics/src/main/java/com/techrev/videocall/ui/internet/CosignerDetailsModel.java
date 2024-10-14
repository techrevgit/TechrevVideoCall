package com.techrev.videocall.ui.internet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CosignerDetailsModel {

    @SerializedName("total")
    @Expose
    private Integer total;

    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;

    @SerializedName("results")
    @Expose
    private ArrayList<Cosigners> cosigners;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public ArrayList<Cosigners> getCosigners() {
        return cosigners;
    }

    public void setCosigners(ArrayList<Cosigners> cosigners) {
        this.cosigners = cosigners;
    }

    public class Cosigners {

        @SerializedName("requestCosignerId")
        @Expose
        private String requestCosignerId;

        @SerializedName("requestId")
        @Expose
        private String requestId;

        @SerializedName("firstName")
        @Expose
        private String firstName;

        @SerializedName("middleName")
        @Expose
        private String middleName;

        @SerializedName("lastName")
        @Expose
        private String lastName;

        @SerializedName("phone")
        @Expose
        private String phone;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("userId")
        @Expose
        private String userId;

        @SerializedName("phonePrefixCode")
        @Expose
        private String phonePrefixCode;

        @SerializedName("isKbaVerified")
        @Expose
        private String isKbaVerified;

        @SerializedName("kbaVerifiedDateTime")
        @Expose
        private String kbaVerifiedDateTime;

        @SerializedName("isDocumentVerified")
        @Expose
        private String isDocumentVerified;

        @SerializedName("isDocumentVerifiedDateTime")
        @Expose
        private String isDocumentVerifiedDateTime;

        public String getRequestCosignerId() {
            return requestCosignerId;
        }

        public void setRequestCosignerId(String requestCosignerId) {
            this.requestCosignerId = requestCosignerId;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPhonePrefixCode() {
            return phonePrefixCode;
        }

        public void setPhonePrefixCode(String phonePrefixCode) {
            this.phonePrefixCode = phonePrefixCode;
        }

        public String getIsKbaVerified() {
            return isKbaVerified;
        }

        public void setIsKbaVerified(String isKbaVerified) {
            this.isKbaVerified = isKbaVerified;
        }

        public String getKbaVerifiedDateTime() {
            return kbaVerifiedDateTime;
        }

        public void setKbaVerifiedDateTime(String kbaVerifiedDateTime) {
            this.kbaVerifiedDateTime = kbaVerifiedDateTime;
        }

        public String getIsDocumentVerified() {
            return isDocumentVerified;
        }

        public void setIsDocumentVerified(String isDocumentVerified) {
            this.isDocumentVerified = isDocumentVerified;
        }

        public String getIsDocumentVerifiedDateTime() {
            return isDocumentVerifiedDateTime;
        }

        public void setIsDocumentVerifiedDateTime(String isDocumentVerifiedDateTime) {
            this.isDocumentVerifiedDateTime = isDocumentVerifiedDateTime;
        }
    }

}
