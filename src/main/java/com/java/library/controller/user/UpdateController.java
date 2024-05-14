package com.java.library.controller.user;

import com.java.database.DBHandle;
import com.java.formatter.NumberStringFilteredConverter;
import com.java.library.models.DataHolder;
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

public class UpdateController implements Initializable {
    private DBHandle handle;
    @FXML
    private TextField nameText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField ageText;
    @FXML
    private ComboBox<String> role;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handle = DBHandle.getInstance();
        User u = handle.find(DataHolder.getInstance().getIdChange());
        ObservableList<String> roles = FXCollections.observableArrayList("Admin","User");
        role.setItems(roles);

        role.getSelectionModel().select(u==null ? "":u.getRole());
        NumberStringFilteredConverter converter = new NumberStringFilteredConverter();
        final TextFormatter<Number> formatter = new TextFormatter<>(
                converter,
                0,
                converter.getFilter()
        );
        ageText.setTextFormatter(formatter);
        ageText.setText(String.valueOf(u.getAge()));
        emailText.setText(u.getEmail());
        nameText.setText(u.getName());
    }

    public void update(ActionEvent e) throws SQLException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        User u = new User(DataHolder.getInstance().getIdChange());
        u.setName(nameText.getText());
        u.setAge(Integer.parseInt(ageText.getText().replace(",","")));
        u.setRole(role.getSelectionModel().equals("Admin") ? 1:2);
        u.setEmail(emailText.getText());
        try{
            handle.update(u);
        }catch (Exception er){
            er.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setContentText("Cập nhật thành công");
        alert.initOwner(stage);
        alert.showAndWait();
        stage.close();
    }
}
