package com.techrev.videocall.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientActivePlanDetailsByClientIdModel
{
    @SerializedName("results")
    @Expose
    private Results results;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

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
    public class Results {

        @SerializedName("clientId")
        @Expose
        private Integer clientId;
        @SerializedName("isPaid")
        @Expose
        private Integer isPaid;
        @SerializedName("clientName")
        @Expose
        private String clientName;
        @SerializedName("pricingPlanId")
        @Expose
        private Integer pricingPlanId;
        @SerializedName("subscriptionStartDate")
        @Expose
        private String subscriptionStartDate;
        @SerializedName("subscriptionEndDate")
        @Expose
        private String subscriptionEndDate;
        @SerializedName("description")
        @Expose
        private List<String> description = null;
        @SerializedName("docId")
        @Expose
        private Object docId;
        @SerializedName("phonePrefixCode")
        @Expose
        private String phonePrefixCode;
        @SerializedName("timezone")
        @Expose
        private String timezone;
        @SerializedName("primaryAddressId")
        @Expose
        private Object primaryAddressId;
        @SerializedName("taxId")
        @Expose
        private Object taxId;
        @SerializedName("subDomainName")
        @Expose
        private Object subDomainName;
        @SerializedName("isSubDomainActive")
        @Expose
        private Integer isSubDomainActive;
        @SerializedName("isScreenShareEnabled")
        @Expose
        private Integer isScreenShareEnabled;
        @SerializedName("isSalesForceEnabled")
        @Expose
        private List<Integer> isSalesForceEnabled = null;
        @SerializedName("planId")
        @Expose
        private Integer planId;
        @SerializedName("planName")
        @Expose
        private String planName;
        @SerializedName("initialRecharge")
        @Expose
        private Integer initialRecharge;
        @SerializedName("initialCost")
        @Expose
        private Integer initialCost;
        @SerializedName("isActive")
        @Expose
        private Integer isActive;
        @SerializedName("annualFee")
        @Expose
        private Integer annualFee;
        @SerializedName("isFreePlan")
        @Expose
        private Integer isFreePlan;
        @SerializedName("fixedCost")
        @Expose
        private Double fixedCost;
        @SerializedName("participantPerMinute")
        @Expose
        private Double participantPerMinute;
        @SerializedName("recordingCost")
        @Expose
        private Double recordingCost;
        @SerializedName("compositionCost")
        @Expose
        private Double compositionCost;
        @SerializedName("storageCost")
        @Expose
        private Double storageCost;
        @SerializedName("mediaDownloadCost")
        @Expose
        private Integer mediaDownloadCost;
        @SerializedName("smsCost")
        @Expose
        private Double smsCost;
        @SerializedName("marginPercentage")
        @Expose
        private Integer marginPercentage;
        @SerializedName("isSmsAllowed")
        @Expose
        private Integer isSmsAllowed;
        @SerializedName("isRecordingAllowed")
        @Expose
        private Integer isRecordingAllowed;
        @SerializedName("MaxMeetingDuration")
        @Expose
        private Integer maxMeetingDuration;
        @SerializedName("isSubdomainEnabled")
        @Expose
        private Integer isSubdomainEnabled;
        @SerializedName("MaxParticipantLimit")
        @Expose
        private Integer maxParticipantLimit;
        @SerializedName("isEmailAllowed")
        @Expose
        private Integer isEmailAllowed;
        @SerializedName("isCloudStorageAvaliable")
        @Expose
        private Integer isCloudStorageAvaliable;
        @SerializedName("MaxMeetingLimit")
        @Expose
        private Integer maxMeetingLimit;
        @SerializedName("MaxImageCaptureLimit")
        @Expose
        private Integer maxImageCaptureLimit;
        @SerializedName("fixedCostWOR")
        @Expose
        private Double fixedCostWOR;
        @SerializedName("minimumCost")
        @Expose
        private Double minimumCost;
        @SerializedName("minimumCostWOR")
        @Expose
        private Double minimumCostWOR;
        @SerializedName("participantPerMinuteWOR")
        @Expose
        private Double participantPerMinuteWOR;
        @SerializedName("perParticipantSMSCount")
        @Expose
        private Integer perParticipantSMSCount;
        @SerializedName("storageDurationInMonths")
        @Expose
        private Integer storageDurationInMonths;

        public Integer getClientId() {
            return clientId;
        }

        public void setClientId(Integer clientId) {
            this.clientId = clientId;
        }

        public Integer getIsPaid() {
            return isPaid;
        }

        public void setIsPaid(Integer isPaid) {
            this.isPaid = isPaid;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public Integer getPricingPlanId() {
            return pricingPlanId;
        }

        public void setPricingPlanId(Integer pricingPlanId) {
            this.pricingPlanId = pricingPlanId;
        }

        public String getSubscriptionStartDate() {
            return subscriptionStartDate;
        }

        public void setSubscriptionStartDate(String subscriptionStartDate) {
            this.subscriptionStartDate = subscriptionStartDate;
        }

        public String getSubscriptionEndDate() {
            return subscriptionEndDate;
        }

        public void setSubscriptionEndDate(String subscriptionEndDate) {
            this.subscriptionEndDate = subscriptionEndDate;
        }

        public List<String> getDescription() {
            return description;
        }

        public void setDescription(List<String> description) {
            this.description = description;
        }

        public Object getDocId() {
            return docId;
        }

        public void setDocId(Object docId) {
            this.docId = docId;
        }

        public String getPhonePrefixCode() {
            return phonePrefixCode;
        }

        public void setPhonePrefixCode(String phonePrefixCode) {
            this.phonePrefixCode = phonePrefixCode;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public Object getPrimaryAddressId() {
            return primaryAddressId;
        }

        public void setPrimaryAddressId(Object primaryAddressId) {
            this.primaryAddressId = primaryAddressId;
        }

        public Object getTaxId() {
            return taxId;
        }

        public void setTaxId(Object taxId) {
            this.taxId = taxId;
        }

        public Object getSubDomainName() {
            return subDomainName;
        }

        public void setSubDomainName(Object subDomainName) {
            this.subDomainName = subDomainName;
        }

        public Integer getIsSubDomainActive() {
            return isSubDomainActive;
        }

        public void setIsSubDomainActive(Integer isSubDomainActive) {
            this.isSubDomainActive = isSubDomainActive;
        }

        public Integer getIsScreenShareEnabled() {
            return isScreenShareEnabled;
        }

        public void setIsScreenShareEnabled(Integer isScreenShareEnabled) {
            this.isScreenShareEnabled = isScreenShareEnabled;
        }

        public List<Integer> getIsSalesForceEnabled() {
            return isSalesForceEnabled;
        }

        public void setIsSalesForceEnabled(List<Integer> isSalesForceEnabled) {
            this.isSalesForceEnabled = isSalesForceEnabled;
        }

        public Integer getPlanId() {
            return planId;
        }

        public void setPlanId(Integer planId) {
            this.planId = planId;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public Integer getInitialRecharge() {
            return initialRecharge;
        }

        public void setInitialRecharge(Integer initialRecharge) {
            this.initialRecharge = initialRecharge;
        }

        public Integer getInitialCost() {
            return initialCost;
        }

        public void setInitialCost(Integer initialCost) {
            this.initialCost = initialCost;
        }

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }

        public Integer getAnnualFee() {
            return annualFee;
        }

        public void setAnnualFee(Integer annualFee) {
            this.annualFee = annualFee;
        }

        public Integer getIsFreePlan() {
            return isFreePlan;
        }

        public void setIsFreePlan(Integer isFreePlan) {
            this.isFreePlan = isFreePlan;
        }

        public Double getFixedCost() {
            return fixedCost;
        }

        public void setFixedCost(Double fixedCost) {
            this.fixedCost = fixedCost;
        }

        public Double getParticipantPerMinute() {
            return participantPerMinute;
        }

        public void setParticipantPerMinute(Double participantPerMinute) {
            this.participantPerMinute = participantPerMinute;
        }

        public Double getRecordingCost() {
            return recordingCost;
        }

        public void setRecordingCost(Double recordingCost) {
            this.recordingCost = recordingCost;
        }

        public Double getCompositionCost() {
            return compositionCost;
        }

        public void setCompositionCost(Double compositionCost) {
            this.compositionCost = compositionCost;
        }

        public Double getStorageCost() {
            return storageCost;
        }

        public void setStorageCost(Double storageCost) {
            this.storageCost = storageCost;
        }

        public Integer getMediaDownloadCost() {
            return mediaDownloadCost;
        }

        public void setMediaDownloadCost(Integer mediaDownloadCost) {
            this.mediaDownloadCost = mediaDownloadCost;
        }

        public Double getSmsCost() {
            return smsCost;
        }

        public void setSmsCost(Double smsCost) {
            this.smsCost = smsCost;
        }

        public Integer getMarginPercentage() {
            return marginPercentage;
        }

        public void setMarginPercentage(Integer marginPercentage) {
            this.marginPercentage = marginPercentage;
        }

        public Integer getIsSmsAllowed() {
            return isSmsAllowed;
        }

        public void setIsSmsAllowed(Integer isSmsAllowed) {
            this.isSmsAllowed = isSmsAllowed;
        }

        public Integer getIsRecordingAllowed() {
            return isRecordingAllowed;
        }

        public void setIsRecordingAllowed(Integer isRecordingAllowed) {
            this.isRecordingAllowed = isRecordingAllowed;
        }

        public Integer getMaxMeetingDuration() {
            return maxMeetingDuration;
        }

        public void setMaxMeetingDuration(Integer maxMeetingDuration) {
            this.maxMeetingDuration = maxMeetingDuration;
        }

        public Integer getIsSubdomainEnabled() {
            return isSubdomainEnabled;
        }

        public void setIsSubdomainEnabled(Integer isSubdomainEnabled) {
            this.isSubdomainEnabled = isSubdomainEnabled;
        }

        public Integer getMaxParticipantLimit() {
            return maxParticipantLimit;
        }

        public void setMaxParticipantLimit(Integer maxParticipantLimit) {
            this.maxParticipantLimit = maxParticipantLimit;
        }

        public Integer getIsEmailAllowed() {
            return isEmailAllowed;
        }

        public void setIsEmailAllowed(Integer isEmailAllowed) {
            this.isEmailAllowed = isEmailAllowed;
        }

        public Integer getIsCloudStorageAvaliable() {
            return isCloudStorageAvaliable;
        }

        public void setIsCloudStorageAvaliable(Integer isCloudStorageAvaliable) {
            this.isCloudStorageAvaliable = isCloudStorageAvaliable;
        }

        public Integer getMaxMeetingLimit() {
            return maxMeetingLimit;
        }

        public void setMaxMeetingLimit(Integer maxMeetingLimit) {
            this.maxMeetingLimit = maxMeetingLimit;
        }

        public Integer getMaxImageCaptureLimit() {
            return maxImageCaptureLimit;
        }

        public void setMaxImageCaptureLimit(Integer maxImageCaptureLimit) {
            this.maxImageCaptureLimit = maxImageCaptureLimit;
        }

        public Double getFixedCostWOR() {
            return fixedCostWOR;
        }

        public void setFixedCostWOR(Double fixedCostWOR) {
            this.fixedCostWOR = fixedCostWOR;
        }

        public Double getMinimumCost() {
            return minimumCost;
        }

        public void setMinimumCost(Double minimumCost) {
            this.minimumCost = minimumCost;
        }

        public Double getMinimumCostWOR() {
            return minimumCostWOR;
        }

        public void setMinimumCostWOR(Double minimumCostWOR) {
            this.minimumCostWOR = minimumCostWOR;
        }

        public Double getParticipantPerMinuteWOR() {
            return participantPerMinuteWOR;
        }

        public void setParticipantPerMinuteWOR(Double participantPerMinuteWOR) {
            this.participantPerMinuteWOR = participantPerMinuteWOR;
        }

        public Integer getPerParticipantSMSCount() {
            return perParticipantSMSCount;
        }

        public void setPerParticipantSMSCount(Integer perParticipantSMSCount) {
            this.perParticipantSMSCount = perParticipantSMSCount;
        }

        public Integer getStorageDurationInMonths() {
            return storageDurationInMonths;
        }

        public void setStorageDurationInMonths(Integer storageDurationInMonths) {
            this.storageDurationInMonths = storageDurationInMonths;
        }

    }
}
