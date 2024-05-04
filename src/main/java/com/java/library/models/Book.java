package com.java.library.models;

import java.util.ArrayList;

public class Book {
    private int id;
    private String name;
    private String author;
    private int amount;
    private String desc;
    private ArrayList<BookCategory> categories;
    private String categoriesString;

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

    public ArrayList<BookCategory> getCategories() {
        return categories;
    }

    public ArrayList<Integer> getCategoryIds(){
        ArrayList<Integer> res = new ArrayList<>();
        categories.forEach(category -> {
            res.add(category.getId());
        });
        return res;
    }

    public String getCategoriesString() {
        return categoriesString;
    }

    public void setCategories(ArrayList<BookCategory> categories) {
        this.categories = categories;
        StringBuilder buffer = new StringBuilder();
        int size = categories.size();
        System.out.println(size);
        for (int i = 0;i< size-1;i++){
            buffer.append(categories.get(i).getName());
            buffer.append(", ");
        }
        if(size > 0) buffer.append(categories.getLast().getName());
        this.categoriesString = buffer.toString();
    }
}
