package com.java.library.models;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

public class Rent {
    private int id;
    private int bookId;
    private int userId;
    private Timestamp rentDate;
    private Date returnDate;

    public Rent(){}

    public Rent(int id, int bookId, int userId) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        rentDate = new Timestamp(Calendar.getInstance().getTime().getTime());
    }
    public Rent( int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
        rentDate = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getRentDate() {
        return rentDate;
    }

    public void setRentDate(Timestamp rentDate) {
        this.rentDate = rentDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
