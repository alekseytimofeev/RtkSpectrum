package widget.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import widget.Parentable;

import java.net.URL;
import java.util.*;

public class RootController implements Initializable {

    public String str = "1233";
    private Map<String, Parentable> childs = new HashMap<>();

    public static String CONTROL_PANE_NAME          = "ControlPane";
    public static String DATA_BASE_PANE_NAME        = "DataBasePane";
    public static String IDEAL_MODELS_PANE_NAME     = "IdealModelsPane";
    public static String MEASUREMENT_PANE_NAME      = "MeasurementPane";
    public static String OBSERVATION_PANE_NAME      = "ObservationPane";
    public static String PARAMETERS_PANE_NAME       = "ParametersPane";
    public static String PROCESSING_PANE_NAME       = "ProcessingPane";
    public static String STORAGE_PANE_NAME          = "StoragePane";


    @FXML
    public Button btn;

    @FXML
    public Label lbl;

    @FXML
    private void onButtonClickWorkDB() {

        //ExecutorBDcommands.addComand();

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data(1, 10));
        series.getData().add(new XYChart.Data(2, 11));
        series.getData().add(new XYChart.Data(3, 12));
        series.getData().add(new XYChart.Data(4, 13));
        series.getData().add(new XYChart.Data(5, 14));
        ObservationController parentable = (ObservationController) childs.get(OBSERVATION_PANE_NAME);
        parentable.showGraph(series, 0, 10);
    }

    public RootController() {
        System.out.println("Widget RootController\t" + Thread.currentThread().getName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Widget RootController initialize\t" + Thread.currentThread().getName());
    }

    public void addChild(String name, Parentable child) {
        childs.put(name, child);
    }

}
