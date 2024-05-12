package com.java.library.controller.rent;

import com.java.database.DBHandle;
import com.java.library.models.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ReturnController implements Initializable {
    private DBHandle dbHandle;
    private ObservableList<Rent> rents;
    @FXML
    private TextField nameText;
    @FXML
    private TableView<Rent> rentTable;
    @FXML
    private TableColumn<Rent, String> userNameCol;
    @FXML
    private TableColumn<Rent, String> bookNameCol;
    @FXML
    private TableColumn<Rent, String> rentDateCol;
    @FXML
    private Button returnBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbHandle  = DBHandle.getInstance();
        rents = FXCollections.observableArrayList();
        userNameCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("username"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("book"));
        rentDateCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("rentDate"));
        rentTable.setItems(rents);

    }

    @FXML
    public void  onReturn(){

    }
}
