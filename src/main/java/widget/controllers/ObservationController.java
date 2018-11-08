package widget.controllers;

import detectionModules.BlockDetectionCommands;
import detectionModules.BlockDetectionCommands.StartMeasure;
import detectionModules.BlockDetectionController.MeasureData;
import detectionModules.ExecutorBlockDetectionCommands;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import widget.Parentable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ObservationController implements Initializable, Parentable{

    private RootController parent;

    @FXML
    private LineChart<?, ?> chart;

    @FXML
    private NumberAxis axisSpectr;

    @FXML
    private NumberAxis axisChannels;

    public void showGraph(MeasureData data) {
        System.out.println("showGraph");

        XYChart.Series series = new XYChart.Series();
        short max = 0;
        for (int i=0; i<data.getData().size(); i++) {
            series.getData().add(new XYChart.Data(i+1, data.getData().get(i)));
            if(max<data.getData().get(i))
                max = data.getData().get(i);
        }

        chart.getData().clear();
        axisSpectr.setLowerBound(0);
        axisSpectr.setUpperBound(max);
        axisChannels.setLowerBound(0);
        axisChannels.setUpperBound(data.getData().size());
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

    @FXML
    public void onBtnImportClick() {
        System.out.println("onBtnImportClick");

        ExecutorBlockDetectionCommands.addCommand(
                new BlockDetectionCommands.ImportMeasureData(parent.getBdController(), (byte)1));

    }


    @Override
    public void setParent(RootController parent) {
        this.parent = parent;
    }
}
