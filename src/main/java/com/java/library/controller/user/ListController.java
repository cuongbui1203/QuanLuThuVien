package com.java.library.controller.user;

import com.java.database.DBHandle;
import com.java.library.fxml.FxmlFile;
import com.java.library.models.DataHolder;
import com.java.library.models.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
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
            ResultSet res = dbHandle.all(DBHandle.USERS);
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
        accountTable.getSelectionModel().selectedItemProperty().addListener(new WeakChangeListener<User>(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observableValue, User user, User t1) {
                DataHolder dataHolder = DataHolder.getInstance();
                if(t1 == null){
                    dataHolder.setIdChange(-1);
                }else {
                    dataHolder.setIdChange(t1.getId());
                }
            }
        }));
    }
    @FXML
    public void refresh(){
        loadData();
    }

    @FXML
    public void edit(ActionEvent e) throws IOException {
        Stage mainStage =(Stage) ((Node)e.getSource()).getScene().getWindow();
        FXMLLoader register = new FXMLLoader(Objects.requireNonNull(FxmlFile.class.getResource("updateUser.fxml")));
        Scene scene = new Scene(register.load());
        Stage stage = new Stage();
        stage.setTitle("Cập nhật");
        stage.initOwner(mainStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
}
