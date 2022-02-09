package com.example.vichat.Models;







public class statusModel {
    private String userid;
    private String username;
    private String ename;
   private  String statusUrl;

    public statusModel() {

    }

    public statusModel(String username, String userid, String ename, String statusUrl
    ) {
        this.userid = userid;
        this.username = username;
        this.ename = ename;
        this.statusUrl=statusUrl;


    }

    public String getStatusUrl() {
        return statusUrl;
    }

    public void setStatusUrl(String statusUrl) {
        this.statusUrl = statusUrl;
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

