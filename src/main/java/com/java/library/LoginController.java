package com.java.library;

import com.java.Database.DBHandle;
import com.java.library.models.DataHolder;
import com.java.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    TextField email;

    @FXML
    PasswordField password;

    @FXML
    Button loginBtn;

    private DBHandle handler;

    public void handleLogin(ActionEvent e){
        User user = handler.login(email.getText(),password.getText());
        System.out.println(user);
        DataHolder dataHolder = DataHolder.getInstance();
        dataHolder.setUser(user);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DBHandle.getInstance();
    }
}
