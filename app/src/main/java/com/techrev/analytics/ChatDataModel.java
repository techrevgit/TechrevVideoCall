package com.techrev.analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatDataModel {

    @Expose
    @SerializedName("results")
    private List<ChatData> chatDataList;

    public List<ChatData> getChatDataList() {
        return chatDataList;
    }

    public void setChatDataList(List<ChatData> chatDataList) {
        this.chatDataList = chatDataList;
    }

    public static class ChatData {
        @Expose
        @SerializedName("messageId")
        private String messageId;

        @Expose
        @SerializedName("meetingId")
        private String meetingId;

        @Expose
        @SerializedName("messageType")
        private String messageType;

        @Expose
        @SerializedName("message")
        private String message;

        @Expose
        @SerializedName("senderId")
        private String senderId;

        @Expose
        @SerializedName("senderType")
        private String senderType;

        @Expose
        @SerializedName("senderName")
        private String senderName;

        @Expose
        @SerializedName("receiverId")
        private String receiverId;

        @Expose
        @SerializedName("receiverType")
        private String receiverType;

        @Expose
        @SerializedName("receiverName")
        private String receiverName;

        @Expose
        @SerializedName("docId")
        private String docId;

        @Expose
        @SerializedName("createdAt")
        private String createdAt;

        @Expose
        @SerializedName("docName")
        private String docName;

        @Expose
        @SerializedName("OriginalFileName")
        private String OriginalFileName;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(String meetingId) {
            this.meetingId = meetingId;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getSenderType() {
            return senderType;
        }

        public void setSenderType(String senderType) {
            this.senderType = senderType;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getReceiverType() {
            return receiverType;
        }

        public void setReceiverType(String receiverType) {
            this.receiverType = receiverType;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDocName() {
            return docName;
        }

        public void setDocName(String docName) {
            this.docName = docName;
        }

        public String getOriginalFileName() {
            return OriginalFileName;
        }

        public void setOriginalFileName(String originalFileName) {
            OriginalFileName = originalFileName;
        }
    }
}
