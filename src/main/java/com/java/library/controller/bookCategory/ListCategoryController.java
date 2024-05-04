package com.java.library.controller.bookCategory;

import com.java.database.DBHandle;
import com.java.library.models.BookCategory;
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

public class ListCategoryController implements Initializable {
    private DBHandle dbHandle;
    private ObservableList<BookCategory> bookCategories;
    @FXML
    private TextField nameText;
    @FXML
    private TableView<BookCategory> categoryTable;
    @FXML
    private TableColumn<BookCategory,Integer> idCol;
    @FXML
    private TableColumn<BookCategory,String> nameCol;
    @FXML
    private TableColumn<BookCategory,Integer> amountCol;

    private void loadData(){
        bookCategories.clear();
        try {
            ResultSet res = dbHandle.all(DBHandle.CATEGORIES);
            while (res.next()){
                bookCategories.add(new BookCategory(res.getInt("id"), res.getString("name"),res.getInt("amount")));
            }
        } catch (SQLException e) {
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookCategories = FXCollections.observableArrayList();
        dbHandle = DBHandle.getInstance();
        idCol.setCellValueFactory(new PropertyValueFactory<BookCategory,Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<BookCategory,String>("name"));
        amountCol.setCellValueFactory(new PropertyValueFactory<BookCategory,Integer>("amountBook"));
        categoryTable.setItems(bookCategories);
        loadData();
    }

    public void saveCategory(){
        BookCategory bookCategory = new BookCategory();
        bookCategory.setName(nameText.getText());
        try {
            dbHandle.insert(bookCategory);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            loadData();
        }
    }

}
