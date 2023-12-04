package com.techrev.analytics;

public class DataModel {

    private String from;
    private String to;
    private String messageType;
    private String content;

    //Newly Added for Boolean
    private Boolean messageValue;

    public Boolean getMessageValue() {
        return messageValue;
    }
    public void setMessageValue(Boolean messageValue) {
        this.messageValue = messageValue;
    }

    public DataModel(){
    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}