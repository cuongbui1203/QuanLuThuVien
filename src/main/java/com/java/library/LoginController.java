package com.java.library;

import com.java.Database.DBHandle;
import com.java.library.fxml.FxmlFile;
import com.java.library.models.DataHolder;
import com.java.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    TextField email;

    @FXML
    PasswordField password;

    @FXML
    Button loginBtn;

    private DBHandle handler;

    public void handleLogin(ActionEvent e) throws IOException {
        User user = handler.login(email.getText(),password.getText());
        Stage stage = (Stage) (((Node) e.getSource()).getScene().getWindow());
        if(user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(stage);
            alert.getDialogPane().setHeaderText("Login fail");
            alert.getDialogPane().setContentText("try agent");
            alert.showAndWait();
        }else{
            DataHolder dataHolder = DataHolder.getInstance();
            dataHolder.setUser(user);
            FXMLLoader main = new FXMLLoader(Objects.requireNonNull(FxmlFile.class.getResource("main.fxml")));
            stage.setScene(new Scene(main.load()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DBHandle.getInstance();
    }
}
