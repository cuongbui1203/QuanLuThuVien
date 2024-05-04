package com.java.library.models;

import com.java.database.DBHandle;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {

    protected int id;
    protected String name;
    protected int age;
    protected String email;
    protected int role;

    public User(int id, String name, int age, String email, int role) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = role;
    }
    public User(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role == 1 ? "Admin":"User";
    }
    public int getRoleId(){
        return role;
    }
    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

}
