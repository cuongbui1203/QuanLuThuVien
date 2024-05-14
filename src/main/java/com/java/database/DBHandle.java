package com.java.database;


import com.java.hash.BCrypt;
import com.java.library.models.Book;
import com.java.library.models.BookCategory;
import com.java.library.models.Rent;
import com.java.library.models.User;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        } catch (SQLException ignored) {
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
        try {
            String query = MessageFormat.format("SELECT * FROM {0} WHERE id=?", DBHandle.USERS);
            PreparedStatement statement1 = conn.prepareStatement(query);
            statement1.setInt(1,id);
            ResultSet res = statement1.executeQuery();
            if(res.next()) {
                userRes =  new User(
                        res.getInt("id"),
                        res.getString("name"),
                        res.getInt("age"),
                        res.getString("email"),
                        res.getInt("role_id")
                );
            }
        } catch (SQLException ignored) {
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
        } catch (SQLException ignored) {
        }
        
        return userRes;
    }

    public ResultSet all(String table) throws SQLException {
        String query;
        if(table.equals(DBHandle.CATEGORIES)){
            query = MessageFormat.format("select c.*,(select count(*) from {0} b where b.category_id=c.id) as amount from {1} c;",DBHandle.BOOK_CATEGORY,DBHandle.CATEGORIES);
        } else if (table.equals(DBHandle.RENTS)) {
            query = MessageFormat.format("select r.*,u.name as user_name,b.name as book_name from {0} r inner join {1} u on user_id = u.id inner join {2} b on r.book_id=b.id where return_date is null;",
                    DBHandle.RENTS,
                    DBHandle.USERS,
                    DBHandle.BOOKS
            );
        } else {
            query = "SELECT * FROM " + table;
        }
        return statement.executeQuery(query);
    }

    public ResultSet allRent() throws SQLException {
        String query = MessageFormat.format("select r.*,u.name as user_name,b.name as book_name from {0} r inner join {1} u on user_id = u.id inner join {2} b on r.book_id=b.id",
                DBHandle.RENTS,
                DBHandle.USERS,
                DBHandle.BOOKS
        );
        return statement.executeQuery(query);
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

    public void insert(@NotNull Rent rent) throws SQLException {
        String sql = MessageFormat.format("INSERT INTO {0}(book_id,user_id,rent_date) VALUES (?,?,?)", DBHandle.RENTS);
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1,rent.getBookId());
        preparedStatement.setInt(2,rent.getUserId());
        preparedStatement.setTimestamp(3,rent.getRentDate());
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

    public boolean returnBook(int id) throws SQLException {
        String query = MessageFormat.format("Update {0} set return_date = ? where id = ?",DBHandle.RENTS);
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setTimestamp(1,new Timestamp(Calendar.getInstance().getTime().getTime()));
        preparedStatement.setInt(2,id);
        return preparedStatement.execute();
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
    public void update(@NotNull User u) throws SQLException {
        String query = MessageFormat.format("UPDATE {0} SET name = ?,age = ?,role_id = ? where id = ?",DBHandle.USERS);
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1,u.getName());
        preparedStatement.setInt(2,u.getAge());
        preparedStatement.setInt(3,u.getRoleId());
        preparedStatement.setInt(4,u.getId());
        preparedStatement.executeUpdate();
    }
    public void update(@NotNull Book book) throws SQLException {
        String query = "DELETE FROM "+BOOKS+" WHERE id=?";
        String query1 = "DELETE FROM "+BOOK_CATEGORY+" WHERE book_id=?";
        PreparedStatement preparedStatement =  conn.prepareStatement(query);
        PreparedStatement preparedStatement1 =  conn.prepareStatement(query1);
        preparedStatement.setInt(1,book.getId());
        preparedStatement1.setInt(1,book.getId());
        preparedStatement.execute();
        preparedStatement1.execute();
        insert(book);
    }
    public int countRentBook() throws SQLException {
        String query  = MessageFormat.format("select count(*) as total from {0} where return_date is null",DBHandle.RENTS);
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet res =  preparedStatement.executeQuery();
        res.next();
        return res.getInt("Total");
    }
    public int countReturnBook() throws SQLException {
        String query  = MessageFormat.format("select count(*) as total from {0} where return_date is not null",DBHandle.RENTS);
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet res =  preparedStatement.executeQuery();
        res.next();
        return res.getInt("Total");
    }
    public static void main(String[] args) throws SQLException {
        DBHandle dbHandle = DBHandle.getInstance();
    }

    @Override
    public void close() throws Exception {
        this.conn.close();
    }
}
