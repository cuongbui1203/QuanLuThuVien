package com.java.library.controller.user;

import com.java.library.models.DataHolder;
import com.java.library.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoController implements Initializable {
    @FXML
    private Label name;
    @FXML
    private Label age;
    @FXML
    private Label email;
    @FXML
    private Label role;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User u = DataHolder.getInstance().getUser();
        name.setText(u.getName());
        age.setText(String.valueOf(u.getAge()));
        email.setText(u.getEmail());
        role.setText(u.getRole());
    }
}
