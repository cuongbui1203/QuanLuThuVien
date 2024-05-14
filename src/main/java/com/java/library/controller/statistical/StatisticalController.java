package com.java.library.controller.statistical;

import com.java.database.DBHandle;
import com.java.library.fxml.FxmlFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class StatisticalController implements Initializable {
    private DBHandle dbHandle;
    private ObservableList<PieChart.Data> pieChartData;
    private int totalRentBook;
    private int totalReturnBook;
    private VBox chart;
    @FXML
    private PieChart overallChart;
    @FXML
    private Label rentText;
    @FXML
    private Label returnText;
    @FXML
    private ScrollPane main;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            chart = FXMLLoader.load(Objects.requireNonNull(FxmlFile.class.getResource("statisticalChart.fxml")));
            main.setContent(chart);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        main.getChildren().add(FXMLLoader.load(Objects.requireNonNull(FxmlFile.class.getResource("rentBook.fxml"))));
    }

    private void loadData(){

    }

}
