package com.example.vichat.Models;







public class videosModel {
    private String userid;
    private String username;
    private String ename;
    private String videoMessage;
    private String videoUrl;
    private  String likeCount;
    private  String lastSeen;
    private  String statusUrl;

    public videosModel() {

    }

    public videosModel(String username, String userid, String ename, String videoMessage,String videoUrl
                       ,String  likeCount,String lastSeen,String statusUrl
    ) {
        this.userid = userid;
        this.username = username;
        this.ename = ename;
        this.videoMessage=videoMessage;
        this.videoUrl=videoUrl;
        this.likeCount=likeCount;
        this.lastSeen=lastSeen;
        this.statusUrl=statusUrl;



    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoMessage() {
        return videoMessage;
    }

    public void setVideoMessage(String videoMessage) {
        this.videoMessage = videoMessage;
    }

    public String getStatusUrl() {
        return statusUrl;
    }

    public void setStatusUrl(String statusUrl) {
        this.statusUrl = statusUrl;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }


}

