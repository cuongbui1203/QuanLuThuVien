package com.java.Database;


import com.java.Hash.BCrypt;
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
        String url = "jdbc:sqlite:" + ModelSql.class.getResource("database.db").getPath();
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
        } catch (SQLException e) {
            return null;
        }
            return null;
    }
}
