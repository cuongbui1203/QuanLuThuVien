package com.java.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    AnchorPane main;



    @FXML
    public void onListBooks(){

    }

    @FXML
    public void onSearchBook(){
        //
    }

    @FXML
    public void onListAccounts(){
        //
    }

    @FXML
    public void addNewAccount(){
        //
    }

    @FXML
    public void onStatistical(){

    }

    @FXML
    public void onLogin(){
        try {
            Pane loginPanel = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
            main.getChildren().add(loginPanel);
            System.out.print("main:::");
            System.out.println(main.getScene().getWindow().getUserData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
