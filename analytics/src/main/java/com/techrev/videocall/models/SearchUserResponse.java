package com.techrev.videocall.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchUserResponse {

    @SerializedName("results")
    private List<SearchUser> results;

    public List<SearchUser> getResults() {
        return results;
    }

    public void setResults(List<SearchUser> results) {
        this.results = results;
    }

    public class SearchUser {
        @SerializedName("userId")
        private String userId;
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("MiddleName")
        private String MiddleName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("email")
        private String email;
        @SerializedName("Phone")
        private String Phone;
        @SerializedName("displayName")
        private String displayName;
        @SerializedName("addressId")
        private String addressId;
        @SerializedName("ProfilePictureDocId")
        private String ProfilePictureDocId;
        @SerializedName("phonePrefixCode")
        private String phonePrefixCode;
        @SerializedName("addressline1")
        private String addressline1;
        @SerializedName("addressline2")
        private String addressline2;
        @SerializedName("country")
        private String country;
        @SerializedName("state")
        private String state;
        @SerializedName("city")
        private String city;
        @SerializedName("pincode")
        private String pincode;

        public String getUserId() {
            return userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getMiddleName() {
            return MiddleName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return Phone;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getAddressId() {
            return addressId;
        }

        public String getProfilePictureDocId() {
            return ProfilePictureDocId;
        }

        public String getPhonePrefixCode() {
            return phonePrefixCode;
        }

        public String getAddressline1() {
            return addressline1;
        }

        public String getAddressline2() {
            return addressline2;
        }

        public String getCountry() {
            return country;
        }

        public String getState() {
            return state;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setMiddleName(String middleName) {
            MiddleName = middleName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public void setProfilePictureDocId(String profilePictureDocId) {
            ProfilePictureDocId = profilePictureDocId;
        }

        public void setPhonePrefixCode(String phonePrefixCode) {
            this.phonePrefixCode = phonePrefixCode;
        }

        public void setAddressline1(String addressline1) {
            this.addressline1 = addressline1;
        }

        public void setAddressline2(String addressline2) {
            this.addressline2 = addressline2;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }
    }

}
