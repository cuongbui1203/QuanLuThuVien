package com.java.library.controller.book;

import com.java.database.DBHandle;
import com.java.formatter.NumberStringFilteredConverter;
import com.java.library.models.Book;
import com.java.library.models.BookCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private TableColumn<Book,String> categoriesCol;
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
    @FXML
    private HBox hBox;
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

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        booksList = FXCollections.observableArrayList();
        dbHandle = DBHandle.getInstance();
        nameCol.setCellValueFactory(new PropertyValueFactory<Book,String>("name"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        descCol.setCellValueFactory(new PropertyValueFactory<Book,String>("desc"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Book,Integer>("amount"));
        categoriesCol.setCellValueFactory(new PropertyValueFactory<Book,String>("categoriesString"));
        bookTable.setItems(booksList);

        ObservableList<BookCategory> categoriesList = FXCollections.observableArrayList();
        try {
            ResultSet res = dbHandle.all(DBHandle.CATEGORIES);
            while (res.next()){
                categoriesList.add(new BookCategory(res.getInt("id"),res.getString("name")));
            }
        } catch (SQLException e) {
        }

        categoriesChoice = new CheckComboBox<>(categoriesList);
        categoriesChoice.setPrefWidth(200);
        categoriesChoice.setMaxWidth(200);
        hBox.getChildren().add(categoriesChoice);
        NumberStringFilteredConverter converter = new NumberStringFilteredConverter();
        final TextFormatter<Number> formatter = new TextFormatter<>(
                converter,
                0,
                converter.getFilter()
        );
        amountText.setTextFormatter(formatter);
        loadData();
    }

    public void addBook() {
        Book book = new Book();
        book.setName(nameText.getText());
        book.setAuthor(authorText.getText());
        book.setDesc(descText.getText());
        book.setAmount(Integer.parseInt(amountText.getText().replace(",","")));
        book.setCategories(new ArrayList<>(categoriesChoice.getCheckModel().getCheckedItems()));
        try {
            dbHandle.insert(book);
            nameText.setText("");
            authorText.setText("");
            descText.setText("");
            amountText.setText("0");
            categoriesChoice.getCheckModel().clearChecks();
        } catch (SQLException e) {
        } finally {
            loadData();
        }
    }

    public void deleteBook() throws SQLException {
        dbHandle.delete(bookTable.getSelectionModel().getSelectedItem());
        loadData();
    }
}
