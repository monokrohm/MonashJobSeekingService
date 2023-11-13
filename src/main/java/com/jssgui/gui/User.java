package com.jssgui.gui;

import java.util.Date;

public class User {
    private String address;
    private String city;
    private String country;
    private Date dateOfBirth;
    private String education;
    private String email;
    private String firstName;
    private String graduation;
    private String lastName;
    private String password;
    private String phoneNo;
    private String post;
    private String skills;
    private String state;
    private int userID;
    private String username;
    private int userTypeID;

    public User(){
    }

    /*public static User getInstance(){
        return instance;
    }*/

    public String getAddress(){
        return this.address;
    }

    public String getCity(){
        return this.city;
    }

    public String getCountry(){
        return this.country;
    }

    public Date getDateOfBirth(){
        return this.dateOfBirth;
    }

    public String getEducation(){
        return this.education;
    }

    public String getEmail(){
        return this.email;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getGraduation(){
        return this.graduation;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getPassword(){
        return this.password;
    }

    public String getPhoneNo(){
        return this.phoneNo;
    }

    public String getPost(){
        return this.post;
    }

    public String getSkills(){
        return this.skills;
    }

    public String getState(){
        return this.state;
    }

    public int getUserID(){
        return userID;
    }

    public String getUsername(){
        return username;
    }

    public int getUserTypeID(){
        return userTypeID;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public void setDateOfBirth(Date dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public void setEducation(String education){
        this.education = education;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setGraduation(String graduation){
        this.graduation = graduation;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setPhoneNo(String phoneNo){
        this.phoneNo = phoneNo;
    }

    public void setPost(String post){
        this.post = post;
    }

    public void setSkills(String skills){
        this.skills = skills;
    }

    public void setState(String state){
        this.state = state;
    }

    public void setUserID(int userID){
        this.userID = userID;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setUserTypeID(int userTypeID){
        this.userTypeID = userTypeID;
    }
}
