package widget.controllers;

import detectionModules.BlockDetectionController.MeasureData;
import detectionModules.BlockDetectionController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import widget.Parentable;

import java.net.URL;
import java.util.*;

public class RootController implements Initializable {

    public static Map<String, Parentable> childs = new HashMap<>();
    private BlockDetectionController blockDetectionController;

    public static String CONTROL_PANE_NAME          = "ControlPane";
    public static String DATA_BASE_PANE_NAME        = "DataBasePane";
    public static String IDEAL_MODELS_PANE_NAME     = "IdealModelsPane";
    public static String MEASUREMENT_PANE_NAME      = "MeasurementPane";
    public static String OBSERVATION_PANE_NAME      = "ObservationPane";
    public static String PARAMETERS_PANE_NAME       = "ParametersPane";
    public static String PROCESSING_PANE_NAME       = "ProcessingPane";
    public static String STORAGE_PANE_NAME          = "StoragePane";
    public static String HEADER_PANE_NAME          = "HeaderPane";
    public static String BOTTOM_PANE_NAME          = "BottomPane";

    public BlockDetectionController getBdController() {
        return blockDetectionController;
    }

    public void setBlockDetectionController(BlockDetectionController blockDetectionController)  {
        this.blockDetectionController = blockDetectionController;
    }

    @FXML
    public Button btn;

    @FXML
    public Label lbl;

    @FXML
    private void onButtonClickWorkDB() {
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

    public static void showGraph(MeasureData data) {
        Platform.runLater(() ->((ObservationController)(childs.get(OBSERVATION_PANE_NAME))).showGraph(data));
    }

    public static void addFoundedDevice(byte logicNumber, int serialNumber) {
        Platform.runLater(() ->((HeaderController)(childs.get(HEADER_PANE_NAME))).addFoundedDevice(logicNumber, serialNumber));
    }

    public static void addLog(BottomController.Log log) {
        Platform.runLater(() ->((BottomController)(childs.get(BOTTOM_PANE_NAME))).addLod(log));
    }

}
