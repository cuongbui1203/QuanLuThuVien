package com.java.library.controller.rent;

import com.java.database.DBHandle;
import com.java.library.models.Book;
import com.java.library.models.BookCategory;
import com.java.library.models.Rent;
import com.java.library.models.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RentController implements Initializable {
    private DBHandle dbHandle;
    private ObservableList<User> users;
    private ObservableList<User> usersRes;
    private ObservableList<Book> books;
    private ObservableList<Book> booksRes;
    private int bookSelectedId;
    private int userSelectedId;
    @FXML
    private TextField userNameText;
    @FXML
    private TextField bookNameText;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book,String> bookNameCol;
    @FXML
    private TableColumn<Book,String> categoriesCol;
    @FXML
    private TableColumn<Book,Integer> amountBookCol;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User,String> userNameCol;
    @FXML
    private TableColumn<User,Integer> userIdCol;
    @FXML
    private TableColumn<User,String> userEmailCol;
    @FXML
    private Button rentBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookSelectedId = -1;
        userSelectedId = -1;
        rentBtn.setDisable(true);
        dbHandle = DBHandle.getInstance();
        users = FXCollections.observableArrayList();
        usersRes = FXCollections.observableArrayList();
        books = FXCollections.observableArrayList();
        booksRes = FXCollections.observableArrayList();
        userNameCol.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        userTable.setItems(usersRes);
        bookNameCol.setCellValueFactory(new PropertyValueFactory<Book,String>("name"));
        categoriesCol.setCellValueFactory(new PropertyValueFactory<Book,String>("categories"));
        amountBookCol.setCellValueFactory(new PropertyValueFactory<Book,Integer>("amount"));
        bookTable.setItems(booksRes);

        try(ResultSet res = dbHandle.all(DBHandle.USERS)){
            while (res.next()){
                users.add(
                        new User(
                                res.getInt("id"),
                                res.getString("name"),
                                res.getInt("age"),
                                res.getString("email"),
                                res.getInt("role_id")
                        )
                );
            }
        } catch (SQLException ignored) {
        }
        usersRes.addAll(users);

        try(ResultSet res = dbHandle.all(DBHandle.BOOKS)){
                books.clear();
                while (res.next()) {
                    Book newBook = new Book(
                            res.getInt("id"),
                            res.getString("name"),
                            res.getString("author"),
                            res.getInt("amount"),
                            res.getString("description")
                    );
                    try (ResultSet categoryRes = dbHandle.getAllCategories(newBook)) {
                        ArrayList<BookCategory> categories = new ArrayList<>();
                        while (categoryRes.next()) {
                            categories.add(new BookCategory(categoryRes.getInt("id"), categoryRes.getString("name")));
                        }
                        newBook.setCategories(categories);
                    }

                    books.add(newBook);
                }

        } catch (SQLException ignored) {
        }
        booksRes.addAll(books);

        userNameText.textProperty().addListener(new WeakChangeListener<String>(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                usersRes.clear();
                users.forEach(user -> {
                    if(user.getName().contains(t1)){
                        usersRes.add(user);
                    }
                });
            }
        }));

        bookNameText.textProperty().addListener(new WeakChangeListener<String>(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                booksRes.clear();
                books.forEach(book -> {
                    if(book.getName().contains(t1)){
                        booksRes.add(book);
                    }
                });
            }
        }));

        userTable.getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<User>(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observableValue, User user, User t1) {
                if(t1 != null){
                    userSelectedId = t1.getId();
                }
                if(userSelectedId > 0 && bookSelectedId > 0){
                    rentBtn.setDisable(false);
                }
            }
        }));
        bookTable.getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<Book>(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observableValue, Book book, Book t1) {
                if(t1 != null){
                    bookSelectedId = t1.getId();
                }
                if(userSelectedId > 0 && bookSelectedId > 0){
                    rentBtn.setDisable(false);
                }
            }
        }));
    }
    public void onRent(ActionEvent event){
        Rent rent = new Rent(bookSelectedId,userSelectedId);
        try {
            dbHandle.insert(rent);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"");
            alert.initOwner(((Node)event.getSource()).getScene().getWindow());
            alert.getDialogPane().setHeaderText("Mượn thành công");
//            alert.getDialogPane().setContentText(MessageFormat.format("Người dùng {0} đã mượn 1 cuốn sách {1}",user.getName(),book.getName()));
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"");
            alert.getDialogPane().setContentText("Có lỗi sảy ra");
            alert.showAndWait();
        }
    }
}
