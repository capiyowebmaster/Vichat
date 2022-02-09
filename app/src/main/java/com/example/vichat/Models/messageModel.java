package com.example.vichat.Models;







public class messageModel {
    private String senderid;
    private String receiverid;
    private String caption;
    private String sendImages;
    private  String messages;
    private  String lastSeen;
    private  String statusUrl;
    private  String time;

    public messageModel() {

    }

    public messageModel(String senderid,String lastSeen,String statusUrl,String time,String receiverid,String messages, String caption, String sendImages
    ) {

        this.messages=messages;
        this.senderid=senderid;
        this.lastSeen=lastSeen;
        this.receiverid=receiverid;
        this.caption = caption;
        this.sendImages = sendImages;
        this.statusUrl=statusUrl;
        this.time=time;

    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getSenderid() {
        return senderid;
    }

    public String getStatusUrl() {
        return statusUrl;
    }

    public void setStatusUrl(String statusUrl) {
        this.statusUrl = statusUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getSendImages() {
        return sendImages;
    }

    public void setSendImages(String sendImages) {
        this.sendImages = sendImages;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }
}

