package com.java.library;

import com.java.library.controller.BookListController;
import com.java.library.fxml.FxmlFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader MainLoader = new FXMLLoader(FxmlFile.class.getResource("login.fxml"));
        Scene scene = new Scene(MainLoader.load());
        stage.setTitle("Quản lý thư viện");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}