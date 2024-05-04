package com.java.database;


import com.java.hash.BCrypt;
import com.java.library.models.Book;
import com.java.library.models.BookCategory;
import com.java.library.models.User;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public class DBHandle implements AutoCloseable {
    private static DBHandle instance;
    private Connection conn;
    private Statement statement;
    private final String url = "jdbc:sqlite:res/database/database.db";

    public static final String BOOKS = "books";
    public static final String CATEGORIES = "categories";
    public static final String BOOK_CATEGORY = "book_category";
    public static final String USERS = "users";
    public static final String RENTS = "rents";

    private DBHandle(){
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

    public User find(int id){
        
        User userRes = null;
        String query = MessageFormat.format("SELECT * FROM {0} WHERE id=?", DBHandle.USERS);
        try {
            PreparedStatement statement1 = this.conn.prepareStatement(query);
            statement1.setInt(1,id);
            ResultSet res = this.statement.executeQuery(query);
            if(res.next()) {
                userRes =  new User(
                        res.getInt("id"),
                        res.getString("name"),
                        res.getInt("age"),
                        res.getString("email"),
                        res.getInt("role_id")
                );
            }
        } catch (SQLException e) {
        }
        
        return userRes;

    }

    public User login(String email,String password){
        
        String query = MessageFormat.format("SELECT * FROM {0} WHERE email=?", DBHandle.USERS);
        User userRes = null;
        try {
            PreparedStatement statement1 = this.conn.prepareStatement(query);
            statement1.setString(1,email);
            ResultSet res = statement1.executeQuery();
            if(res.next()) {
                String hashedPassword = res.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    userRes = new User(
                            res.getInt("id"),
                            res.getString("name"),
                            res.getInt("age"),
                            res.getString("email"),
                            res.getInt("role_id")
                    );
                }
            }
        } catch (SQLException e) {
        }
        
        return userRes;
    }

    public ResultSet all(String table) throws SQLException {
        String query;
        if(table.equals(DBHandle.CATEGORIES)){
            query = MessageFormat.format("select c.*,(select count(*) from {0} b where b.category_id=c.id) as amount from {1} c;",DBHandle.BOOK_CATEGORY,DBHandle.CATEGORIES);
        }else {
            query = "SELECT * FROM " + table;
        }
        ResultSet res = statement.executeQuery(query);
        return res;
    }

    public void insert(@NotNull BookCategory category) throws SQLException {
        
        String query = MessageFormat.format("INSERT INTO {0}(name) values(?)",DBHandle.CATEGORIES);
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1,category.getName());
        preparedStatement.executeUpdate();
        
    }

    /**
     * insert new book to books table
     * @param book
     * @throws SQLException
     */
    public void insert(@NotNull Book book) throws SQLException {
        
        // create query
        String sql = MessageFormat.format("INSERT INTO {0}(name,author,description,amount) VALUES (?,?,?,?)", DBHandle.BOOKS);
        StringBuilder sql2Builder = new StringBuilder(MessageFormat.format("INSERT INTO {0}(book_id,category_id) VALUES", DBHandle.BOOK_CATEGORY));
        int size = book.getCategories().size();
        ArrayList<BookCategory> categories = book.getCategories();
        for (int i = 0;i<size-1;i++){
            sql2Builder.append("(?,?)");
            sql2Builder.append(",");
        }
        sql2Builder.append("(?,?)");
        String sql2 = sql2Builder.toString();

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        PreparedStatement preparedStatement1 = conn.prepareStatement(sql2);
        PreparedStatement preparedStatement2 = conn.prepareStatement("Select id from books where name=? and author=? and description=?");

        preparedStatement2.setString(1,book.getName());
        preparedStatement2.setString(2,book.getAuthor());
        preparedStatement2.setString(3,book.getDesc());

        preparedStatement.setString(1, book.getName());
        preparedStatement.setString(2, book.getAuthor());
        preparedStatement.setString(3, book.getDesc());
        preparedStatement.setInt(4,book.getAmount());

        preparedStatement.executeUpdate();
        // get new book id for relationships
        ResultSet res = preparedStatement2.executeQuery();
        res.next();
        int id = res.getInt("id");
        for (int i = 0;i<size;i++){
            preparedStatement1.setInt(i*2+1,id);
            preparedStatement1.setInt(i*2+2,categories.get(i).getId());
        }

        preparedStatement1.executeUpdate();

    }


    public void insert(@NotNull User user,String password) throws SQLException {
        String sql = MessageFormat.format("INSERT INTO {0}(name,email,password,age,role_id) VALUES (?,?,?,?,?)", DBHandle.USERS);
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1,user.getName());
        preparedStatement.setString(2,user.getEmail());
        preparedStatement.setString(3,BCrypt.hashpw(password,BCrypt.gensalt()));
        preparedStatement.setInt(4,user.getAge());
        preparedStatement.setInt(5,user.getRoleId());

        preparedStatement.executeUpdate();
    }

    public boolean delete(String table,int id) throws SQLException {
        String query = "DELETE FROM "+table+" WHERE id=?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1,id);
        return preparedStatement.execute();
    }

    public void delete(@NotNull Book book) throws SQLException {
        String query = "DELETE FROM "+DBHandle.BOOKS+" WHERE id=?";
        String query2 = "DELETE FROM "+DBHandle.BOOK_CATEGORY+" WHERE book_id=?";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        PreparedStatement preparedStatement2 = conn.prepareStatement(query2);

        preparedStatement.setInt(1,book.getId());
        preparedStatement2.setInt(1,book.getId());

        preparedStatement.execute();
        preparedStatement2.execute();
    }

    public ResultSet searchBook(String name)throws SQLException {
        String query = MessageFormat.format("SELECT * FROM {0} WHERE name LIKE ''%{1}%''", DBHandle.BOOKS, name);
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public ResultSet getAllCategories(@NotNull Book book) throws SQLException {
        String query = MessageFormat.format("SELECT c.* FROM {0} b inner join {1} c on b.category_id=c.id where book_id=?", DBHandle.BOOK_CATEGORY, DBHandle.CATEGORIES);
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1,book.getId());
        return preparedStatement.executeQuery();
    }

    public static void main(String[] args) throws SQLException {
        DBHandle dbHandle = DBHandle.getInstance();
    }

    @Override
    public void close() throws Exception {
        System.out.println("connection close");
        this.conn.close();
    }
}
