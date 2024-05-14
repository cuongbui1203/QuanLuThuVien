package com.java.library.models;

public class DataHolder {
    private User user;
    private int IdChange;
    private DataHolder(){}
    private final static DataHolder INSTANCE = new DataHolder();

    public static DataHolder getInstance(){
        return INSTANCE;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public int getIdChange() {
        return IdChange;
    }

    public void setIdChange(int idChange) {
        IdChange = idChange;
    }
}
