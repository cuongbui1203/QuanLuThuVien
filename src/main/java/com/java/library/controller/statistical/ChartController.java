package com.java.library.controller.statistical;

import com.java.database.DBHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChartController implements Initializable {
    private int totalRentBook;
    private int totalReturnBook;
    private DBHandle dbHandle;
    private ObservableList<PieChart.Data> pieChartData;
    @FXML
    private PieChart overallChart;
    @FXML
    private Label rentText;
    @FXML
    private Label returnText;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbHandle = DBHandle.getInstance();
        try {
            totalRentBook = dbHandle.countRentBook();
            totalReturnBook = dbHandle.countReturnBook();
        } catch (SQLException ignored) {

        }
        rentText.setText(String.valueOf(totalRentBook));
        returnText.setText(String.valueOf(totalReturnBook));
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Tổng số sách đã trả", totalReturnBook),
                new PieChart.Data("Tổng số sách đang mượn", totalRentBook)
        );
        overallChart.setData(pieChartData);

    }
}
