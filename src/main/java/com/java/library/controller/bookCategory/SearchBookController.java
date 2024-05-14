package com.java.library.controller.bookCategory;

import com.java.database.DBHandle;
import com.java.library.models.Book;
import com.java.library.models.BookCategory;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class SearchBookController implements Initializable {
    private DBHandle dbHandle;
    private ObservableList<Book> booksList;
    private ObservableList<Book> booksListRes;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book,String> nameCol;
    @FXML
    private TableColumn<Book,String> authorCol;
    @FXML
    private TableColumn<Book,String> descCol;
    @FXML
    private TableColumn<Book,String> categoriesCol;
    @FXML
    private TableColumn<Book,Integer> amountCol;
    @FXML
    private CheckComboBox<BookCategory> categoriesChoice;

    private void loadData(){
        try (ResultSet bookRes = dbHandle.all(DBHandle.BOOKS);){
            booksList.clear();
            while (bookRes.next()){
                Book newBook = new Book(
                        bookRes.getInt("id"),
                        bookRes.getString("name"),
                        bookRes.getString("author"),
                        bookRes.getInt("amount"),
                        bookRes.getString("description")
                );
                try(ResultSet categoryRes = dbHandle.getAllCategories(newBook);){
                    ArrayList<BookCategory> categories = new ArrayList<>();
                    while (categoryRes.next()){
                        categories.add(new BookCategory(categoryRes.getInt("id"),categoryRes.getString("name")));
                    }
                    newBook.setCategories(categories);
                }

                booksList.add(newBook);
            }
        } catch (SQLException e) {
            return;
        }
        booksListRes.addAll(booksList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        booksList = FXCollections.observableArrayList();
        booksListRes = FXCollections.observableArrayList();
        dbHandle = DBHandle.getInstance();
        nameCol.setCellValueFactory(new PropertyValueFactory<Book,String>("name"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        descCol.setCellValueFactory(new PropertyValueFactory<Book,String>("desc"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Book,Integer>("amount"));
        categoriesCol.setCellValueFactory(new PropertyValueFactory<Book,String>("categoriesString"));
        bookTable.setItems(booksListRes);
        loadData();
        ObservableList<BookCategory> categoriesList = FXCollections.observableArrayList();
        try {
            ResultSet res = dbHandle.all(DBHandle.CATEGORIES);
            while (res.next()){
                categoriesList.add(new BookCategory(res.getInt("id"),res.getString("name")));
            }
        } catch (SQLException e) {
        }

        categoriesChoice.getItems().addAll(categoriesList);
        categoriesChoice.getCheckModel().getCheckedItems().addListener(new WeakListChangeListener<BookCategory>(new ListChangeListener<BookCategory>() {
            @Override
            public void onChanged(Change<? extends BookCategory> change) {
                onSearch();
            }
        }));

    }

    public void onSearch(){
        ArrayList<Integer> ids = new ArrayList<>();
        LinkedList<BookCategory> categories = new LinkedList<>(categoriesChoice.getCheckModel().getCheckedItems());
        categories.forEach(e->{
            ids.add(e.getId());
        });
        booksListRes.clear();
        booksList.forEach(book -> {
            if(book.getCategoryIds().containsAll(ids)){
                booksListRes.add(book);
            }
        });
    }
}
