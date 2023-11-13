package com.jssgui.gui;

public class Recruiter extends User {
    private String companyAddress;
    private String companyCity;
    private String companyCountry;
    private String companyEmail;
    private int companyID;
    private String companyName;
    private String companyPhoneNo;
    private String companyPost;
    private String companyState;

    public String getCompanyAddress(){
        return this.companyAddress;
    }

    public String getCompanyCity(){
        return this.companyCity;
    }

    public String getCompanyCountry(){
        return this.companyCountry;
    }

    public String getCompanyEmail(){
        return this.companyEmail;
    }

    public int getCompanyID(){
        return this.companyID;
    }

    public String getCompanyName(){
        return this.companyName;
    }

    public String getCompanyPhoneNo(){
        return this.companyPhoneNo;
    }

    public String getCompanyPost(){
        return this.companyPost;
    }

    public String getCompanyState(){
        return this.companyState;
    }

    public void setCompanyAddress(String companyAddress){
        this.companyAddress = companyAddress;
    }

    public void setCompanyCity(String companyCity){
        this.companyCity = companyCity;
    }

    public void setCompanyCountry(String companyCountry){
        this.companyCountry = companyCountry;
    }

    public void setCompanyEmail(String companyEmail){
        this.companyEmail = companyEmail;
    }

    public void setCompanyID(int companyID){
        this.companyID = companyID;
    }

    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    public void setCompanyPhoneNo(String companyPhoneNo){
        this.companyPhoneNo = companyPhoneNo;
    }

    public void setCompanyPost(String companyPost){
        this.companyPost = companyPost;
    }

    public void setCompanyState(String companyState){
        this.companyState = companyState;
    }
}

