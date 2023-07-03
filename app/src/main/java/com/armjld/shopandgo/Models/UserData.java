package com.armjld.shopandgo.Models;

import java.io.Serializable;
import java.util.Date;

public class UserData implements Serializable {
    String id = "";
    String imageUrl = "";
    String name = "";
    String googleId = "";
    String email = "";
    Date date = new Date();

    public UserData() {
    }

    public UserData(String id, String imageUrl, String name, String email, String googleId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.email = email;
        this.googleId = googleId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
