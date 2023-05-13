package com.health.threat.awareness.hospital.model;

public class Hospital {
    private String Email;
    private String Id;
    private String Mobile;
    private String Name;

    public Hospital(String email, String id, String mobile, String name) {
        Email = email;
        Id = id;
        Mobile = mobile;
        Name = name;
    }

    public Hospital() {

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
