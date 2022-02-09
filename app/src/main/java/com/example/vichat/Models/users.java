package com.example.vichat.Models;







public class users {
    private String userid;
    private String username;
    private String ename;
    private String password;


    public users() {

    }

    public users(String username,String password, String userid, String ename
    ) {
        this.userid = userid;
        this.username = username;
        this.ename = ename;


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

