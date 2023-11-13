package com.jssgui.gui;

public class UserInstance{
    private Recruiter rec;
    private Seeker seek;
    private int type;
    private User user;
    private static final UserInstance instance = new UserInstance();

    private UserInstance(){}

    public static UserInstance getInstance(){
        return instance;
    }

    public Recruiter getRec(){
        return this.rec;
    }

    public Seeker getSeek(){
        return this.seek;
    }

    public int getType(){
        return this.type;
    }

    public User getUser(){
        return this.user;
    }

    public void setRec(Recruiter u){
        this.rec = u;
    }

    public void setSeek(Seeker u){
        this.seek = u;
    }

    public void setType(int type){
        this.type = type;
    }

    public void setUser(User u){
        this.user = u;
    }
}
