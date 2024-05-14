package com.java.library.controller.rent;

import com.java.database.DBHandle;
import com.java.library.models.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    private ObservableList<Rent> rents;
    private DBHandle dbHandle;
    @FXML
    private TableView<Rent> rentTable;
    @FXML
    private TableColumn<Rent, String> userNameCol;
    @FXML
    private TableColumn<Rent, String> bookNameCol;
    @FXML
    private TableColumn<Rent, String> rentDateCol;
    @FXML
    private TableColumn<Rent, String> returnDateCol;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rents = FXCollections.observableArrayList();
        dbHandle = DBHandle.getInstance();
        userNameCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("userName"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("bookName"));
        rentDateCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("rentDate"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("returnDate"));
        rentTable.setItems(rents);
        try(ResultSet res = dbHandle.allRent()) {
            while (res.next()){
                Rent rent = new Rent(
                        res.getInt("id"),
                        res.getInt("book_id"),
                        res.getString("book_name"),
                        res.getInt("user_id"),
                        res.getString("user_name")
                );
                rent.setRentDate(res.getTimestamp("rent_date"));
                rent.setReturnDate(res.getTimestamp("return_date"));
                rents.add(rent);
            }
        } catch (Exception ignored){

        }
    }
}
