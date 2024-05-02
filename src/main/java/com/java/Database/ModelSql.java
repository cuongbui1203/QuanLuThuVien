package com.java.Database;

import com.java.library.models.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class ModelSql  {
    private Connection conn;
    protected String table;
    private Statement statement;

    public ModelSql(String table)
    {
        this.table = table;
        connect();
    }
    public void executeFile(String path)
    {
        try (FileReader reader = new FileReader(path);
             // Wrap the FileReader in a BufferedReader for
             // efficient reading.
             BufferedReader bufferedReader
                     = new BufferedReader(reader);
             // Establish a connection to the database.

             // Create a statement object to execute SQL
             // commands.
             Statement statement
                     = conn.createStatement();) {

            System.out.println("Executing commands at : "
                    + path);

            StringBuilder builder = new StringBuilder();

            String line;
            int lineNumber = 0;
            int count = 0;

            // Read lines from the SQL file until the end of the
            // file is reached.
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber += 1;
                line = line.trim();

                // Skip empty lines and single-line comments.
                if (line.isEmpty() || line.startsWith("--"))
                    continue;

                builder.append(line);
                // If the line ends with a semicolon, it
                // indicates the end of an SQL command.
                if (line.endsWith(";"))
                    try {
                        // Execute the SQL command
                        statement.execute(builder.toString());
                        // Print a success message along with
                        // the first 15 characters of the
                        // executed command.
                        System.out.println(
                                ++count
                                        + " Command successfully executed : "
                                        + builder.substring(
                                        0,
                                        Math.min(builder.length(), 15))
                                        + "...");
                        builder.setLength(0);
                    }
                    catch (SQLException e) {
                        // If an SQLException occurs during
                        // execution, print an error message and
                        // stop further execution.
                        System.err.println(
                                "At line " + lineNumber + " : "
                                        + e.getMessage() + "\n");
                        return;
                    }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }


    public void connect() {
        String url = "jdbc:sqlite:" + ModelSql.class.getResource("database.db").getPath();
        try {
            this.conn = DriverManager.getConnection(url);
            this.statement = this.conn.createStatement();
            System.out.println("connect");
//            executeFile(Model.class.getResource("schema.sql").getPath());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Object find(int id) {
        System.out.println(id);
        String sql = "Select * from "+this.table;
        try {
            ResultSet res = statement.executeQuery(sql);
            System.out.println(res.getObject(1, User.class));
            if(res.getInt(1) != 0) return true;
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
