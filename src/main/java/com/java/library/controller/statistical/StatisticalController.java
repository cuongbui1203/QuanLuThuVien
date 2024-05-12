package com.java.library.controller.statistical;

import com.java.database.DBHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticalController implements Initializable {
    DBHandle dbHandle;
    ObservableList<PieChart.Data> pieChartData;
    @FXML
    private PieChart overallChart;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbHandle = DBHandle.getInstance();
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30));
        overallChart.setData(pieChartData);
    }

    private void loadData(){

    }

}
