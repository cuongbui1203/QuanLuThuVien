package com.java.library.controller.book;

import com.java.Database.DBHandle;
import com.java.library.models.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    private DBHandle dbHandle;
    private ObservableList<Book> booksList;
    @FXML
    private TableView<Book>  bookTable;
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
    @FXML
    private TextField authorText;
    @FXML
    private TextField amountText;
    @FXML
    private TextArea descText;
    @FXML
    private Button addBtn;
    @FXML
    private Button deleteBtn;

    private void loadData(){
        try {
            ResultSet bookRes = dbHandle.all("book");
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
        } catch (SQLException e) {
            return;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        booksList = FXCollections.observableArrayList();
        dbHandle = DBHandle.getInstance();
        nameCol.setCellValueFactory(new PropertyValueFactory<Book,String>("name"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        descCol.setCellValueFactory(new PropertyValueFactory<Book,String>("desc"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Book,Integer>("amount"));
        bookTable.setItems(booksList);
        loadData();
    }

    public void addBook() {
        Book book = new Book();
        book.setName(nameText.getText());
        book.setAuthor(authorText.getText());
        book.setDesc(descText.getText());
        book.setAmount(Integer.parseInt(amountText.getText()));
        try {
            dbHandle.insert(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            loadData();
        }
    }

    public void deleteBook() throws SQLException {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        dbHandle.delete("book",selected.getId());
        loadData();
    }
}
