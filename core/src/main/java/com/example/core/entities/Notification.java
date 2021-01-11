package com.example.core.entities;

import java.util.Date;

public class Notification {
    public String notificationId;
    public String recieverId;
    public String senderId;
    public String postId;
    public NotificationType type;
    public Date timestamp;
    public boolean isRed;

    public Notification(String notificationId, String recieverId, String senderId, String postId, NotificationType type, Date timestamp, boolean isRed) {
        this.notificationId = notificationId;
        this.recieverId = recieverId;
        this.senderId = senderId;
        this.postId = postId;
        this.type = type;
        this.timestamp = timestamp;
        this.isRed = isRed;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }
}
