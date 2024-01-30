package com.techrev.videocall.models;

import java.util.List;

public class SearchUserResponse {

    private List<SearchUser> results;

    public List<SearchUser> getResults() {
        return results;
    }

    public class SearchUser {
        private String userId;
        private String firstName;
        private String MiddleName;
        private String lastName;
        private String email;
        private String Phone;
        private String displayName;
        private String addressId;
        private String ProfilePictureDocId;
        private String phonePrefixCode;
        private String addressline1;
        private String addressline2;
        private String country;
        private String state;
        private String city;
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
    }

}
