package com.example.e_learningbyarmelymg.domain;

import java.util.Date;

public class ChatMessage {
    public String senderId, receiverId, message;
    public Date dateTime;

    public ChatMessage(String senderId, String receiverId, String message, Date dateTime){
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.dateTime = dateTime;
    }

    public ChatMessage(){

    }
    public String getMessage() {
        return message;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
