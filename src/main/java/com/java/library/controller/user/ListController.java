package com.java.library.controller.user;

import com.java.Database.DBHandle;
import com.java.library.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    private DBHandle dbHandle;
    private ObservableList<User> accountList;
    @FXML
    private TableView<User> accountTable;
    @FXML
    private TableColumn<User,String> nameCol;
    @FXML
    private TableColumn<User,Integer> idCol;
    @FXML
    private TableColumn<User,Integer> ageCol;
    @FXML
    private TableColumn<User,String> roleCol;
    @FXML
    private TableColumn<User,String> emailCol;

    private void loadData(){
        try {
            ResultSet res = dbHandle.all("user");
            accountList.clear();
            while (res.next()){
                accountList.add(
                        new User(
                                res.getInt("id"),
                                res.getString("name"),
                                res.getInt("age"),
                                res.getString("email"),
                                res.getInt("role_id")
                                )
                );
            }
        } catch (SQLException e) {
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountList = FXCollections.observableArrayList();
        dbHandle = DBHandle.getInstance();
        idCol.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        ageCol.setCellValueFactory(new PropertyValueFactory<User,Integer>("age"));
        roleCol.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
        nameCol.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        accountTable.setItems(accountList);
        loadData();
    }
}
