package com.techrev.analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestForMeetingRoomModel
{
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("twilioSid")
    @Expose
    private String twilioSid;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("joinTime")
    @Expose
    private String joinTime;
    @SerializedName("passcode")
    @Expose
    private String passcode;
    @SerializedName("encryptedPasscode")
    @Expose
    private String encryptedPasscode;
    @SerializedName("expiresAt")
    @Expose
    private String expiresAt;
    @SerializedName("scheduledJoinTime")
    @Expose
    private String scheduledJoinTime;
    @SerializedName("scheduledExpiryTime")
    @Expose
    private String scheduledExpiryTime;
    @SerializedName("scheduledMeetingDuration")
    @Expose
    private Integer scheduledMeetingDuration;
    @SerializedName("isRecordingEnabled")
    @Expose
    private Integer isRecordingEnabled;
    @SerializedName("isSmsEnabled")
    @Expose
    private Integer isSmsEnabled;
    @SerializedName("meetingTitle")
    @Expose
    private String meetingTitle;
    @SerializedName("isNotifyByEmail")
    @Expose
    private Boolean isNotifyByEmail;
    @SerializedName("isNotifyBySMS")
    @Expose
    private Boolean isNotifyBySMS;
    @SerializedName("customerId")
    @Expose
    private Object customerId;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("middleName")
    @Expose
    private Object middleName;
    @SerializedName("lastName")
    @Expose
    private Object lastName;
    @SerializedName("phone")
    @Expose
    private Object phone;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("customerSocketId")
    @Expose
    private Object customerSocketId;
    @SerializedName("appUserSocketId")
    @Expose
    private Object appUserSocketId;
    @SerializedName("appuserfirstName")
    @Expose
    private String appuserfirstName;
    @SerializedName("appuserlastName")
    @Expose
    private String appuserlastName;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("clientName")
    @Expose
    private String clientName;
    @SerializedName("clientId")
    @Expose
    private Integer clientId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("docId")
    @Expose
    private Object docId;
    @SerializedName("isScreenShareEnabled")
    @Expose
    private Integer isScreenShareEnabled;
    @SerializedName("isSalesForceEnabled")
    @Expose
    private Integer isSalesForceEnabled;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTwilioSid() {
        return twilioSid;
    }

    public void setTwilioSid(String twilioSid) {
        this.twilioSid = twilioSid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getEncryptedPasscode() {
        return encryptedPasscode;
    }

    public void setEncryptedPasscode(String encryptedPasscode) {
        this.encryptedPasscode = encryptedPasscode;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getScheduledJoinTime() {
        return scheduledJoinTime;
    }

    public void setScheduledJoinTime(String scheduledJoinTime) {
        this.scheduledJoinTime = scheduledJoinTime;
    }

    public String getScheduledExpiryTime() {
        return scheduledExpiryTime;
    }

    public void setScheduledExpiryTime(String scheduledExpiryTime) {
        this.scheduledExpiryTime = scheduledExpiryTime;
    }

    public Integer getScheduledMeetingDuration() {
        return scheduledMeetingDuration;
    }

    public void setScheduledMeetingDuration(Integer scheduledMeetingDuration) {
        this.scheduledMeetingDuration = scheduledMeetingDuration;
    }

    public Integer getIsRecordingEnabled() {
        return isRecordingEnabled;
    }

    public void setIsRecordingEnabled(Integer isRecordingEnabled) {
        this.isRecordingEnabled = isRecordingEnabled;
    }

    public Integer getIsSmsEnabled() {
        return isSmsEnabled;
    }

    public void setIsSmsEnabled(Integer isSmsEnabled) {
        this.isSmsEnabled = isSmsEnabled;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public Boolean getIsNotifyByEmail() {
        return isNotifyByEmail;
    }

    public void setIsNotifyByEmail(Boolean isNotifyByEmail) {
        this.isNotifyByEmail = isNotifyByEmail;
    }

    public Boolean getIsNotifyBySMS() {
        return isNotifyBySMS;
    }

    public void setIsNotifyBySMS(Boolean isNotifyBySMS) {
        this.isNotifyBySMS = isNotifyBySMS;
    }

    public Object getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Object customerId) {
        this.customerId = customerId;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getMiddleName() {
        return middleName;
    }

    public void setMiddleName(Object middleName) {
        this.middleName = middleName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getCustomerSocketId() {
        return customerSocketId;
    }

    public void setCustomerSocketId(Object customerSocketId) {
        this.customerSocketId = customerSocketId;
    }

    public Object getAppUserSocketId() {
        return appUserSocketId;
    }

    public void setAppUserSocketId(Object appUserSocketId) {
        this.appUserSocketId = appUserSocketId;
    }

    public String getAppuserfirstName() {
        return appuserfirstName;
    }

    public void setAppuserfirstName(String appuserfirstName) {
        this.appuserfirstName = appuserfirstName;
    }

    public String getAppuserlastName() {
        return appuserlastName;
    }

    public void setAppuserlastName(String appuserlastName) {
        this.appuserlastName = appuserlastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getDocId() {
        return docId;
    }

    public void setDocId(Object docId) {
        this.docId = docId;
    }

    public Integer getIsScreenShareEnabled() {
        return isScreenShareEnabled;
    }

    public void setIsScreenShareEnabled(Integer isScreenShareEnabled) {
        this.isScreenShareEnabled = isScreenShareEnabled;
    }

    public Integer getIsSalesForceEnabled() {
        return isSalesForceEnabled;
    }

    public void setIsSalesForceEnabled(Integer isSalesForceEnabled) {
        this.isSalesForceEnabled = isSalesForceEnabled;
    }

}
