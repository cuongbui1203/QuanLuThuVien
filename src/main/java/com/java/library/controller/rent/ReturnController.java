package com.java.library.controller.rent;

import com.java.database.DBHandle;
import com.java.library.models.Rent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReturnController implements Initializable {
    private DBHandle dbHandle;
    private ObservableList<Rent> rents;
    private ObservableList<Rent> resRent;
    private int idRentSelected = -1;
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
    public void loadData(){
        try(ResultSet res = dbHandle.all(DBHandle.RENTS)){
            while (res.next()){
                rents.add(new Rent(
                        res.getInt("id"),
                        res.getInt("book_id"),
                        res.getString("book_name"),
                        res.getInt("user_id"),
                        res.getString("user_name")
                ));
            }
            resRent.addAll(rents);
        } catch (SQLException ignored) {
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbHandle  = DBHandle.getInstance();
        rents = FXCollections.observableArrayList();
        resRent = FXCollections.observableArrayList();
        userNameCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("userName"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("bookName"));
        rentDateCol.setCellValueFactory(new PropertyValueFactory<Rent,String>("rentDate"));
        rentTable.setItems(resRent);
        rentTable.getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<Rent>(new ChangeListener<Rent>() {
            @Override
            public void changed(ObservableValue<? extends Rent> observableValue, Rent rent, Rent t1) {
                idRentSelected = t1.getId();
            }
        }));
        loadData();
    }

    @FXML
    public void  onReturn() throws SQLException {
        if(idRentSelected<0){
            return;
        }
        dbHandle.returnBook(idRentSelected);
        loadData();
    }

    @FXML
    public void onSearch(){
        resRent.clear();
        rents.forEach(rent -> {
            if(rent.getUserName().contains(nameText.getText())){
                resRent.add(rent);
            }
        });
    }
}
