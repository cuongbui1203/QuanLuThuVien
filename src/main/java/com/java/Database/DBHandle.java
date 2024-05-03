package com.java.Database;


import com.java.Hash.BCrypt;
import com.java.library.models.Book;
import com.java.library.models.User;

import java.sql.*;

public class DBHandle {
    private static DBHandle instance;
    private Connection conn;
    private Statement statement;

    private DBHandle(Connection conn){
        this.conn = conn;
    }

    private DBHandle(){
        String url = "jdbc:sqlite:res/database/database.db";
        try {
            this.conn = DriverManager.getConnection(url);
            this.statement = this.conn.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DBHandle getInstance() {
        if(instance == null){
            instance = new DBHandle();
        }
        return instance;
    }

    public static DBHandle getInstance(Connection conn) {
        if(instance == null){
            instance = new DBHandle(conn);
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }

    public User find(int id){
        String query = "SELECT * FROM user WHERE id=?";
        try {
            PreparedStatement statement1 = this.conn.prepareStatement(query);
            statement1.setInt(1,id);
            ResultSet res = this.statement.executeQuery(query);
            if(res.next()) {
                return new User(
                        res.getInt("id"),
                        res.getString("name"),
                        res.getInt("age"),
                        res.getString("email"),
                        res.getInt("role_id")
                );
            }else{
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public User login(String email,String password){
        String query = "SELECT * FROM user WHERE email=?";
        try {
            PreparedStatement statement1 = this.conn.prepareStatement(query);
            statement1.setString(1,email);
            ResultSet res = statement1.executeQuery();
            if(res.next()) {
                String hashedPassword = res.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return new User(
                            res.getInt("id"),
                            res.getString("name"),
                            res.getInt("age"),
                            res.getString("email"),
                            res.getInt("role_id")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet all(String table) throws SQLException {
        String query = "SELECT * FROM "+table;
        return statement.executeQuery(query);
    }

    public void insert(Book book) throws SQLException {
        String sql = "INSERT INTO book(name,author,description,amount) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, book.getName());
        preparedStatement.setString(2, book.getAuthor());
        preparedStatement.setString(3, book.getDesc());
        preparedStatement.setInt(4,book.getAmount());

        preparedStatement.executeUpdate();
    }

    public boolean delete(String table,int id) throws SQLException {
        String query = "DELETE FROM "+table+" WHERE id=?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1,id);
        return preparedStatement.execute();
    }

    public ResultSet searchBook(String name)throws SQLException {
        String query = "SELECT * FROM book WHERE name LIKE '%"+name+"%'";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
//        preparedStatement.setString(1,name);
        return preparedStatement.executeQuery();
    }

    public static void main(String[] args) throws SQLException {
        DBHandle dbHandle = DBHandle.getInstance();
        System.out.println(dbHandle.conn.getSchema());
        System.out.println(dbHandle.all("book"));
    }
}
