package com.java.library.controller.user;

import com.java.database.DBHandle;
import com.java.formatter.NumberStringFilteredConverter;
import com.java.library.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class RegisterController implements Initializable {
    private DBHandle handle;
    @FXML
    private TextField nameText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField ageText;
    @FXML
    private ComboBox<String> role;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button register;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handle = DBHandle.getInstance();

        ObservableList<String> roles = FXCollections.observableArrayList("Admin","User");
        role.setItems(roles);
        role.setId("User");
        role.getSelectionModel().select("User");
        NumberStringFilteredConverter converter = new NumberStringFilteredConverter();
        final TextFormatter<Number> formatter = new TextFormatter<>(
                converter,
                0,
                converter.getFilter()
        );
        ageText.setTextFormatter(formatter);

    }

    public void register(ActionEvent e) throws SQLException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        User u = new User();
        u.setName(nameText.getText());
        u.setAge(Integer.parseInt(ageText.getText().replace(",","")));
        u.setRole(role.getSelectionModel().equals("Admin") ? 1:2);
        u.setEmail(emailText.getText());
        handle.insert(u,passwordField.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setContentText("Đăng ký thành công");
        alert.initOwner(stage);
        alert.showAndWait();
        stage.close();
        System.out.println("after");
    }
}
