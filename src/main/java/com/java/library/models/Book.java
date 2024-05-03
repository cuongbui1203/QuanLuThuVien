package com.java.library.models;

public class Book {
    private int id;
    private String name;
    private String author;
    private int amount;
    private String desc;

    public Book(int id,String name, String author, int amount,String desc) {
        this.desc = desc;
        this.amount = amount;
        this.author = author;
        this.name = name;
        this.id = id;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
