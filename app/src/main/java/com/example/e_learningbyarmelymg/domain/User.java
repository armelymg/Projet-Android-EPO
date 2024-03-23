package com.example.e_learningbyarmelymg.domain;
public class User {
    private String id;
    private String username;
    private String email;
    private String telephone;
    private String password;
    private String photo_url;

    public User(){

    }

    public User(String id, String username, String telephone, String password, String email, String photo_url){
        this.id = id;
        this.username = username;
        this.password = password;
        this.telephone = telephone;
        this.email = email;
        this.photo_url = photo_url;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getUsername() {
        return username;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setId(String id) {
        this.id = id;
    }
}
