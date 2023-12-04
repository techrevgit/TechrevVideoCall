package com.techrev.analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParticipantDetailsModel {

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
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
    public class Result {

        @SerializedName("requestParticipantId")
        @Expose
        private Integer requestParticipantId;
        @SerializedName("requestId")
        @Expose
        private String requestId;
        @SerializedName("participantName")
        @Expose
        private String participantName;
        @SerializedName("ipaddress")
        @Expose
        private String ipaddress;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("participantMeetingId")
        @Expose
        private String participantMeetingId;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("middleName")
        @Expose
        private String middleName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("isCreatedParticipant")
        @Expose
        private Integer isCreatedParticipant;

        public Integer getRequestParticipantId() {
            return requestParticipantId;
        }

        public void setRequestParticipantId(Integer requestParticipantId) {
            this.requestParticipantId = requestParticipantId;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getParticipantName() {
            return participantName;
        }

        public void setParticipantName(String participantName) {
            this.participantName = participantName;
        }

        public String getIpaddress() {
            return ipaddress;
        }

        public void setIpaddress(String ipaddress) {
            this.ipaddress = ipaddress;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getParticipantMeetingId() {
            return participantMeetingId;
        }

        public void setParticipantMeetingId(String participantMeetingId) {
            this.participantMeetingId = participantMeetingId;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Integer getIsCreatedParticipant() {
            return isCreatedParticipant;
        }

        public void setIsCreatedParticipant(Integer isCreatedParticipant) {
            this.isCreatedParticipant = isCreatedParticipant;
        }

    }
}


