package com.java.library;

import com.java.library.fxml.FxmlFile;
import com.java.library.models.DataHolder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private AnchorPane main;
    @FXML
    private Label ruleName;
    @FXML
    private Label accountName;

    @FXML
    public void onListBookCategory() throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(Objects.requireNonNull(FxmlFile.class.getResource("listCategoryBook.fxml"))));
    }

    @FXML
    public void onListBooks() throws IOException {
        Pane listBook = FXMLLoader.load(Objects.requireNonNull(FxmlFile.class.getResource("listBook.fxml")));
        main.getChildren().clear();
        main.getChildren().add(listBook);
    }

    @FXML
    public void onSearchBook() throws IOException {
        Pane searchBook = FXMLLoader.load(Objects.requireNonNull(FxmlFile.class.getResource("searchBook.fxml")));
        main.getChildren().clear();
        main.getChildren().add(searchBook);
    }

    @FXML
    public void onListAccounts() throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(Objects.requireNonNull(FxmlFile.class.getResource("listAccount.fxml"))));
    }

    @FXML
    public void addNewAccount() throws IOException {
        Stage mainStage =(Stage) main.getScene().getWindow();
        FXMLLoader register = new FXMLLoader(Objects.requireNonNull(FxmlFile.class.getResource("register.fxml")));
        Scene scene = new Scene(register.load());
        Stage stage = new Stage();
        stage.setTitle("Đăng ký");
        stage.initOwner(mainStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    public void onStatistical() throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(Objects.requireNonNull(FxmlFile.class.getResource("statistical.fxml"))));
    }

    @FXML
    public void onSearchBookOverCategory() throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(Objects.requireNonNull(FxmlFile.class.getResource("searchBookOverCategory.fxml"))));
    }

    @FXML
    public void onRentBook() throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(Objects.requireNonNull(FxmlFile.class.getResource("rentBook.fxml"))));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DataHolder dataHolder = DataHolder.getInstance();
        ruleName.setText(dataHolder.getUser().getRole());
        accountName.setText(dataHolder.getUser().getName());
        try {
            this.onListBooks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
