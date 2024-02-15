package com.techrev.videocall.models;

import com.google.gson.annotations.SerializedName;

public class RequestDetailsModel {

    @SerializedName("results")
    private Results results;

    @SerializedName("total")
    private int total;

    @SerializedName("pageNumber")
    private int pageNumber;

    // Getters and setters
    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
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

    public class Results {
        @SerializedName("RequestId")
        private String requestId;

        @SerializedName("RequestUserId")
        private int requestUserId;

        @SerializedName("ServiceProviderId")
        private int serviceProviderId;

        @SerializedName("Description")
        private String description;

        @SerializedName("RequestStatus")
        private int requestStatus;

        @SerializedName("TwilioSid")
        private String twilioSid;

        @SerializedName("BaseFee")
        private int baseFee;

        @SerializedName("ExtraSignAndSealFee")
        private int extraSignAndSealFee;

        @SerializedName("ExtraSignAndSeal")
        private int extraSignAndSeal;

        @SerializedName("ExtraFee")
        private int extraFee;

        @SerializedName("Fee")
        private int fee;

        @SerializedName("isPaid")
        private int isPaid;

        @SerializedName("PrimarySignerFee")
        private int primarySignerFee;

        @SerializedName("CosignerFee")
        private int cosignerFee;

        @SerializedName("AdditionalSignSealFee")
        private int additionalSignSealFee;

        @SerializedName("totalCosigners")
        private int totalCosigners;

        @SerializedName("NotarizedDatetime")
        private String notarizedDatetime;

        @SerializedName("VideoCallStatus")
        private int videoCallStatus;

        @SerializedName("paymentProfileId")
        private String paymentProfileId;

        @SerializedName("requestMasterType")
        private int requestMasterType;

        @SerializedName("appoinmentDateTime")
        private String appoinmentDateTime;

        @SerializedName("RequestCreatedTime")
        private String requestCreatedTime;

        @SerializedName("CancelledDateTime")
        private String cancelledDateTime;

        @SerializedName("requestCreatorTypeId")
        private int requestCreatorTypeId;

        @SerializedName("requestCategoryId")
        private int requestCategoryId;

        @SerializedName("requestCancelReason")
        private String requestCancelReason;

        @SerializedName("tempRequestId")
        private String tempRequestId;

        @SerializedName("paymentOptionId")
        private int paymentOptionId;

        @SerializedName("appliedPromotionType")
        private int appliedPromotionType;

        @SerializedName("tempRequestStatus")
        private String tempRequestStatus;

        @SerializedName("mainRequestId")
        private String mainRequestId;

        @SerializedName("Title")
        private String title;

        @SerializedName("requestCategoryName")
        private String requestCategoryName;

        @SerializedName("businessName")
        private String businessName;

        @SerializedName("businessEmail")
        private String businessEmail;

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("lastName")
        private String lastName;

        @SerializedName("MiddleName")
        private String middleName;

        @SerializedName("email")
        private String email;

        @SerializedName("phonePrefixCode")
        private String phonePrefixCode;

        @SerializedName("Phone")
        private String phone;

        @SerializedName("ServiceProviderFirstName")
        private String serviceProviderFirstName;

        @SerializedName("ServiceProviderLastName")
        private String serviceProviderLastName;

        @SerializedName("ServiceProviderMiddleName")
        private String serviceProviderMiddleName;

        @SerializedName("ServiceProviderEmail")
        private String serviceProviderEmail;

        @SerializedName("ServiceProviderPhone")
        private String serviceProviderPhone;

        @SerializedName("ServiceProviderphonePrefixCode")
        private String serviceProviderphonePrefixCode;

        @SerializedName("ServiceProviderProfilePictureDocId")
        private String serviceProviderProfilePictureDocId;

        @SerializedName("userId")
        private int userId;

        @SerializedName("paymentType")
        private int paymentType;

        @SerializedName("bankName")
        private String bankName;

        @SerializedName("promoCodeType")
        private String promoCodeType;

        @SerializedName("discountPrice")
        private String discountPrice;

        @SerializedName("discountPercentageValue")
        private String discountPercentageValue;

        // Getters and setters

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public int getRequestUserId() {
            return requestUserId;
        }

        public void setRequestUserId(int requestUserId) {
            this.requestUserId = requestUserId;
        }

        public int getServiceProviderId() {
            return serviceProviderId;
        }

        public void setServiceProviderId(int serviceProviderId) {
            this.serviceProviderId = serviceProviderId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getRequestStatus() {
            return requestStatus;
        }

        public void setRequestStatus(int requestStatus) {
            this.requestStatus = requestStatus;
        }

        public String getTwilioSid() {
            return twilioSid;
        }

        public void setTwilioSid(String twilioSid) {
            this.twilioSid = twilioSid;
        }

        public int getBaseFee() {
            return baseFee;
        }

        public void setBaseFee(int baseFee) {
            this.baseFee = baseFee;
        }

        public int getExtraSignAndSealFee() {
            return extraSignAndSealFee;
        }

        public void setExtraSignAndSealFee(int extraSignAndSealFee) {
            this.extraSignAndSealFee = extraSignAndSealFee;
        }

        public int getExtraSignAndSeal() {
            return extraSignAndSeal;
        }

        public void setExtraSignAndSeal(int extraSignAndSeal) {
            this.extraSignAndSeal = extraSignAndSeal;
        }

        public int getExtraFee() {
            return extraFee;
        }

        public void setExtraFee(int extraFee) {
            this.extraFee = extraFee;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getIsPaid() {
            return isPaid;
        }

        public void setIsPaid(int isPaid) {
            this.isPaid = isPaid;
        }

        public int getPrimarySignerFee() {
            return primarySignerFee;
        }

        public void setPrimarySignerFee(int primarySignerFee) {
            this.primarySignerFee = primarySignerFee;
        }

        public int getCosignerFee() {
            return cosignerFee;
        }

        public void setCosignerFee(int cosignerFee) {
            this.cosignerFee = cosignerFee;
        }

        public int getAdditionalSignSealFee() {
            return additionalSignSealFee;
        }

        public void setAdditionalSignSealFee(int additionalSignSealFee) {
            this.additionalSignSealFee = additionalSignSealFee;
        }

        public int getTotalCosigners() {
            return totalCosigners;
        }

        public void setTotalCosigners(int totalCosigners) {
            this.totalCosigners = totalCosigners;
        }

        public String getNotarizedDatetime() {
            return notarizedDatetime;
        }

        public void setNotarizedDatetime(String notarizedDatetime) {
            this.notarizedDatetime = notarizedDatetime;
        }

        public int getVideoCallStatus() {
            return videoCallStatus;
        }

        public void setVideoCallStatus(int videoCallStatus) {
            this.videoCallStatus = videoCallStatus;
        }

        public String getPaymentProfileId() {
            return paymentProfileId;
        }

        public void setPaymentProfileId(String paymentProfileId) {
            this.paymentProfileId = paymentProfileId;
        }

        public int getRequestMasterType() {
            return requestMasterType;
        }

        public void setRequestMasterType(int requestMasterType) {
            this.requestMasterType = requestMasterType;
        }

        public String getAppoinmentDateTime() {
            return appoinmentDateTime;
        }

        public void setAppoinmentDateTime(String appoinmentDateTime) {
            this.appoinmentDateTime = appoinmentDateTime;
        }

        public String getRequestCreatedTime() {
            return requestCreatedTime;
        }

        public void setRequestCreatedTime(String requestCreatedTime) {
            this.requestCreatedTime = requestCreatedTime;
        }

        public String getCancelledDateTime() {
            return cancelledDateTime;
        }

        public void setCancelledDateTime(String cancelledDateTime) {
            this.cancelledDateTime = cancelledDateTime;
        }

        public int getRequestCreatorTypeId() {
            return requestCreatorTypeId;
        }

        public void setRequestCreatorTypeId(int requestCreatorTypeId) {
            this.requestCreatorTypeId = requestCreatorTypeId;
        }

        public int getRequestCategoryId() {
            return requestCategoryId;
        }

        public void setRequestCategoryId(int requestCategoryId) {
            this.requestCategoryId = requestCategoryId;
        }

        public String getRequestCancelReason() {
            return requestCancelReason;
        }

        public void setRequestCancelReason(String requestCancelReason) {
            this.requestCancelReason = requestCancelReason;
        }

        public String getTempRequestId() {
            return tempRequestId;
        }

        public void setTempRequestId(String tempRequestId) {
            this.tempRequestId = tempRequestId;
        }

        public int getPaymentOptionId() {
            return paymentOptionId;
        }

        public void setPaymentOptionId(int paymentOptionId) {
            this.paymentOptionId = paymentOptionId;
        }

        public int getAppliedPromotionType() {
            return appliedPromotionType;
        }

        public void setAppliedPromotionType(int appliedPromotionType) {
            this.appliedPromotionType = appliedPromotionType;
        }

        public String getTempRequestStatus() {
            return tempRequestStatus;
        }

        public void setTempRequestStatus(String tempRequestStatus) {
            this.tempRequestStatus = tempRequestStatus;
        }

        public String getMainRequestId() {
            return mainRequestId;
        }

        public void setMainRequestId(String mainRequestId) {
            this.mainRequestId = mainRequestId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRequestCategoryName() {
            return requestCategoryName;
        }

        public void setRequestCategoryName(String requestCategoryName) {
            this.requestCategoryName = requestCategoryName;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getBusinessEmail() {
            return businessEmail;
        }

        public void setBusinessEmail(String businessEmail) {
            this.businessEmail = businessEmail;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhonePrefixCode() {
            return phonePrefixCode;
        }

        public void setPhonePrefixCode(String phonePrefixCode) {
            this.phonePrefixCode = phonePrefixCode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getServiceProviderFirstName() {
            return serviceProviderFirstName;
        }

        public void setServiceProviderFirstName(String serviceProviderFirstName) {
            this.serviceProviderFirstName = serviceProviderFirstName;
        }

        public String getServiceProviderLastName() {
            return serviceProviderLastName;
        }

        public void setServiceProviderLastName(String serviceProviderLastName) {
            this.serviceProviderLastName = serviceProviderLastName;
        }

        public String getServiceProviderMiddleName() {
            return serviceProviderMiddleName;
        }

        public void setServiceProviderMiddleName(String serviceProviderMiddleName) {
            this.serviceProviderMiddleName = serviceProviderMiddleName;
        }

        public String getServiceProviderEmail() {
            return serviceProviderEmail;
        }

        public void setServiceProviderEmail(String serviceProviderEmail) {
            this.serviceProviderEmail = serviceProviderEmail;
        }

        public String getServiceProviderPhone() {
            return serviceProviderPhone;
        }

        public void setServiceProviderPhone(String serviceProviderPhone) {
            this.serviceProviderPhone = serviceProviderPhone;
        }

        public String getServiceProviderphonePrefixCode() {
            return serviceProviderphonePrefixCode;
        }

        public void setServiceProviderphonePrefixCode(String serviceProviderphonePrefixCode) {
            this.serviceProviderphonePrefixCode = serviceProviderphonePrefixCode;
        }

        public String getServiceProviderProfilePictureDocId() {
            return serviceProviderProfilePictureDocId;
        }

        public void setServiceProviderProfilePictureDocId(String serviceProviderProfilePictureDocId) {
            this.serviceProviderProfilePictureDocId = serviceProviderProfilePictureDocId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(int paymentType) {
            this.paymentType = paymentType;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getPromoCodeType() {
            return promoCodeType;
        }

        public void setPromoCodeType(String promoCodeType) {
            this.promoCodeType = promoCodeType;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getDiscountPercentageValue() {
            return discountPercentageValue;
        }

        public void setDiscountPercentageValue(String discountPercentageValue) {
            this.discountPercentageValue = discountPercentageValue;
        }

    }

}
