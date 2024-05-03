package com.java.library.controller.book;

import com.java.Database.DBHandle;
import com.java.library.models.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    private DBHandle dbHandle;
    private ObservableList<Book> booksList;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book,String> nameCol;
    @FXML
    private TableColumn<Book,String> authorCol;
    @FXML
    private TableColumn<Book,String> descCol;
    @FXML
    private TableColumn<Book,Integer> amountCol;
    @FXML
    private TextField nameText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        booksList = FXCollections.observableArrayList();
        dbHandle = DBHandle.getInstance();
        nameCol.setCellValueFactory(new PropertyValueFactory<Book,String>("name"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        descCol.setCellValueFactory(new PropertyValueFactory<Book,String>("desc"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Book,Integer>("amount"));
        bookTable.setItems(booksList);
    }

    @FXML
    public void search() throws SQLException {
        String name = nameText.getText();
        System.out.println(name);
        ResultSet bookRes = dbHandle.searchBook(name);
        booksList.clear();
        while (bookRes.next()){
            booksList.add(
                    new Book(
                            bookRes.getInt("id"),
                            bookRes.getString("name"),
                            bookRes.getString("author"),
                            bookRes.getInt("amount"),
                            bookRes.getString("description")
                    ));
        }
    }
}
