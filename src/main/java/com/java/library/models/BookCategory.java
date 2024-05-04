package com.java.library.models;

public class BookCategory {
    private int id;
    private String name;
    private int amountBook;
    public BookCategory(int id, String name, int amountBook) {
        this.id = id;
        this.name = name;
        this.amountBook = amountBook;
    }
    public BookCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getAmountBook() {
        return amountBook;
    }

    public void setAmountBook(int amountBook) {
        this.amountBook = amountBook;
    }

    public BookCategory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
