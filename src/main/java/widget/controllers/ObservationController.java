package widget.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import widget.Parentable;

import java.net.URL;
import java.util.ResourceBundle;

public class ObservationController implements Initializable, Parentable{

    private RootController parent;

    @FXML
    private LineChart<?, ?> chart;

    @FXML
    private NumberAxis axisSpectr;

    @FXML
    private NumberAxis axisChannels;


    public void showGraph(XYChart.Series series, int lowerBound, int upperBound) {
        chart.getData().clear();
        axisSpectr.setLowerBound(lowerBound);
        axisSpectr.setUpperBound(upperBound);
        axisChannels.setLowerBound(0);
        axisChannels.setUpperBound(series.getData().size());
        chart.getData().addAll(series);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chart.setAnimated(false);
    }

    @FXML
    void onBtnStart(ActionEvent event) {
    }

    @FXML
    void onBtnFinish(ActionEvent event) {
    }


    @Override
    public void setParent(RootController parent) {
        this.parent = parent;
    }
}
