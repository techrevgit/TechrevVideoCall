package com.techrev.analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MeetingDetailsModel {

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


    public class Document {

        @SerializedName("requestId")
        @Expose
        private String requestId;
        @SerializedName("uploadedDocId")
        @Expose
        private String uploadedDocId;
        @SerializedName("createdDateTime")
        @Expose
        private String createdDateTime;
        @SerializedName("documentName")
        @Expose
        private String documentName;
        @SerializedName("DocId")
        @Expose
        private Integer docId;
        @SerializedName("docUrl")
        @Expose
        private String docUrl;

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

        public String getCreatedDateTime() {
            return createdDateTime;
        }

        public void setCreatedDateTime(String createdDateTime) {
            this.createdDateTime = createdDateTime;
        }

        public String getDocumentName() {
            return documentName;
        }

        public void setDocumentName(String documentName) {
            this.documentName = documentName;
        }

        public Integer getDocId() {
            return docId;
        }

        public void setDocId(Integer docId) {
            this.docId = docId;
        }

        public String getDocUrl() {
            return docUrl;
        }

        public void setDocUrl(String docUrl) {
            this.docUrl = docUrl;
        }

    }

    public class GroupingSids {

        @SerializedName("room_sid")
        @Expose
        private String roomSid;
        @SerializedName("participant_sid")
        @Expose
        private String participantSid;

        public String getRoomSid() {
            return roomSid;
        }

        public void setRoomSid(String roomSid) {
            this.roomSid = roomSid;
        }

        public String getParticipantSid() {
            return participantSid;
        }

        public void setParticipantSid(String participantSid) {
            this.participantSid = participantSid;
        }

    }

    public class Links {

        @SerializedName("media")
        @Expose
        private String media;

        public String getMedia() {
            return media;
        }

        public void setMedia(String media) {
            this.media = media;
        }

    }

    public class Recording {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("grouping_sids")
        @Expose
        private GroupingSids groupingSids;
        @SerializedName("container_format")
        @Expose
        private String containerFormat;
        @SerializedName("track_name")
        @Expose
        private String trackName;
        @SerializedName("offset")
        @Expose
        private Double offset;
        @SerializedName("account_sid")
        @Expose
        private String accountSid;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("codec")
        @Expose
        private String codec;
        @SerializedName("source_sid")
        @Expose
        private String sourceSid;
        @SerializedName("sid")
        @Expose
        private String sid;
        @SerializedName("duration")
        @Expose
        private Integer duration;
        @SerializedName("date_created")
        @Expose
        private String dateCreated;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("room_sid")
        @Expose
        private String roomSid;
        @SerializedName("size")
        @Expose
        private Integer size;
        @SerializedName("links")
        @Expose
        private Links links;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public GroupingSids getGroupingSids() {
            return groupingSids;
        }

        public void setGroupingSids(GroupingSids groupingSids) {
            this.groupingSids = groupingSids;
        }

        public String getContainerFormat() {
            return containerFormat;
        }

        public void setContainerFormat(String containerFormat) {
            this.containerFormat = containerFormat;
        }

        public String getTrackName() {
            return trackName;
        }

        public void setTrackName(String trackName) {
            this.trackName = trackName;
        }

        public Double getOffset() {
            return offset;
        }

        public void setOffset(Double offset) {
            this.offset = offset;
        }

        public String getAccountSid() {
            return accountSid;
        }

        public void setAccountSid(String accountSid) {
            this.accountSid = accountSid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCodec() {
            return codec;
        }

        public void setCodec(String codec) {
            this.codec = codec;
        }

        public String getSourceSid() {
            return sourceSid;
        }

        public void setSourceSid(String sourceSid) {
            this.sourceSid = sourceSid;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRoomSid() {
            return roomSid;
        }

        public void setRoomSid(String roomSid) {
            this.roomSid = roomSid;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Links getLinks() {
            return links;
        }

        public void setLinks(Links links) {
            this.links = links;
        }

    }

    public class Results {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("requestId")
        @Expose
        private String requestId;
        @SerializedName("compositionSid")
        @Expose
        private Object compositionSid;
        @SerializedName("compositionStatus")
        @Expose
        private Integer compositionStatus;
        @SerializedName("createdBy")
        @Expose
        private Integer createdBy;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("isInstantMeeting")
        @Expose
        private Integer isInstantMeeting;
        @SerializedName("videoUrl")
        @Expose
        private String videoUrl;
        @SerializedName("encryptedPasscode")
        @Expose
        private String encryptedPasscode;
        @SerializedName("passode")
        @Expose
        private String passode;
        @SerializedName("isBilled")
        @Expose
        private Integer isBilled;
        @SerializedName("cost")
        @Expose
        private Integer cost;
        @SerializedName("meetingTitle")
        @Expose
        private String meetingTitle;
        @SerializedName("sfEventId")
        @Expose
        private Object sfEventId;
        @SerializedName("isRecordingEnabled")
        @Expose
        private Integer isRecordingEnabled;
        @SerializedName("isSmsEnabled")
        @Expose
        private Integer isSmsEnabled;
        @SerializedName("scheduledJoinTime")
        @Expose
        private String scheduledJoinTime;
        @SerializedName("scheduledExpiryTime")
        @Expose
        private String scheduledExpiryTime;
        @SerializedName("scheduledMeetingDuration")
        @Expose
        private Integer scheduledMeetingDuration;
        @SerializedName("twilioSid")
        @Expose
        private String twilioSid;
        @SerializedName("joinTime")
        @Expose
        private String joinTime;
        @SerializedName("expiresAt")
        @Expose
        private String expiresAt;
        @SerializedName("meetingNotes")
        @Expose
        private Object meetingNotes;
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
        @SerializedName("accountNo")
        @Expose
        private Object accountNo;
        @SerializedName("phone")
        @Expose
        private Object phone;
        @SerializedName("latitude")
        @Expose
        private Object latitude;
        @SerializedName("longitude")
        @Expose
        private Object longitude;
        @SerializedName("ipaddress")
        @Expose
        private Object ipaddress;
        @SerializedName("email")
        @Expose
        private Object email;
        @SerializedName("isActive")
        @Expose
        private Object isActive;
        @SerializedName("documents")
        @Expose
        private List<Document> documents = null;
        @SerializedName("recordings")
        @Expose
        private List<Recording> recordings = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public Object getCompositionSid() {
            return compositionSid;
        }

        public void setCompositionSid(Object compositionSid) {
            this.compositionSid = compositionSid;
        }

        public Integer getCompositionStatus() {
            return compositionStatus;
        }

        public void setCompositionStatus(Integer compositionStatus) {
            this.compositionStatus = compositionStatus;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getIsInstantMeeting() {
            return isInstantMeeting;
        }

        public void setIsInstantMeeting(Integer isInstantMeeting) {
            this.isInstantMeeting = isInstantMeeting;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getEncryptedPasscode() {
            return encryptedPasscode;
        }

        public void setEncryptedPasscode(String encryptedPasscode) {
            this.encryptedPasscode = encryptedPasscode;
        }

        public String getPassode() {
            return passode;
        }

        public void setPassode(String passode) {
            this.passode = passode;
        }

        public Integer getIsBilled() {
            return isBilled;
        }

        public void setIsBilled(Integer isBilled) {
            this.isBilled = isBilled;
        }

        public Integer getCost() {
            return cost;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
        }

        public String getMeetingTitle() {
            return meetingTitle;
        }

        public void setMeetingTitle(String meetingTitle) {
            this.meetingTitle = meetingTitle;
        }

        public Object getSfEventId() {
            return sfEventId;
        }

        public void setSfEventId(Object sfEventId) {
            this.sfEventId = sfEventId;
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

        public String getTwilioSid() {
            return twilioSid;
        }

        public void setTwilioSid(String twilioSid) {
            this.twilioSid = twilioSid;
        }

        public String getJoinTime() {
            return joinTime;
        }

        public void setJoinTime(String joinTime) {
            this.joinTime = joinTime;
        }

        public String getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
        }

        public Object getMeetingNotes() {
            return meetingNotes;
        }

        public void setMeetingNotes(Object meetingNotes) {
            this.meetingNotes = meetingNotes;
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

        public Object getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(Object accountNo) {
            this.accountNo = accountNo;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Object getIpaddress() {
            return ipaddress;
        }

        public void setIpaddress(Object ipaddress) {
            this.ipaddress = ipaddress;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getIsActive() {
            return isActive;
        }

        public void setIsActive(Object isActive) {
            this.isActive = isActive;
        }

        public List<Document> getDocuments() {
            return documents;
        }

        public void setDocuments(List<Document> documents) {
            this.documents = documents;
        }

        public List<Recording> getRecordings() {
            return recordings;
        }

        public void setRecordings(List<Recording> recordings) {
            this.recordings = recordings;
        }

    }

}