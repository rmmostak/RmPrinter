package com.rmproduct.rmprinteradmin;


class UserInfo {

    String name, roll, session, uid, email;

    public UserInfo() {

    }

    public UserInfo(String name, String roll, String session, String uid) {
        this.name = name;
        this.roll = roll;
        this.session = session;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getSession() {
        return session;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email=email;
    }
}

