package com.example.e_learningbyarmelymg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Course {
    private String title;
    private String subtitle;
    private String picAdress;

    public Course(String title, String subtitle, String picAdress) {
        this.title = title;
        this.subtitle = subtitle;
        this.picAdress = picAdress;
    }

    public Course(){
    }

    public String getTitle(){
        return this.title;
    }

    public String getSubTitle(){
        return this.subtitle;
    }

    public String getPicAdress(){
        return this.picAdress;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setSubTitle(String subtitle){
        this.subtitle = subtitle;
    }

    public void setPicAdress(String picAdress){
        this.picAdress = picAdress;
    }
}
