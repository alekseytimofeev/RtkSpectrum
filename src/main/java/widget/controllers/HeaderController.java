package widget.controllers;

import detectionModules.BlockDetectionCommands;
import detectionModules.ExecutorBlockDetectionCommands;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.GridPane;
import widget.Parentable;

public class HeaderController implements Parentable, Initializable {

    private RootController parent;

    public static String MEASUREMENT_COMMAND = "Измерение";
    public static String CONTINUING_MEASUREMENT_COMMAND = "Продолжение измерения";
    public static String SOURCE_SEARCH_COMMAND = "Поиск источника";
    public static String CONTINUING_SOURCE_SEARCH_COMMAND = "Продолжение поиск источника";
    public static String CALIBRATION_COMMAND = "Калибровка";
    public static String SETTING_PARAMETERS_COMMAND = "Установка параметров";
    public static String TEMPERATURE_MEASUREMENT_COMMAND = "Измерение температуры";
    public static String READING_SPECTRS_FROM_BOI_COMMAND = "Чтение спектров из БОИ";
    public static String LOADING_DATA_IN_BOI_COMMAND = "Загрузка данных в БОИ";
    public static String DEVICE_SEARCH_COMMAND = "Поиск устройств";

    @FXML
    public Button btnStart;

    @FXML
    public Button btnStop;

    @FXML
    public ChoiceBox choiceCommand;

    @FXML
    public GridPane boxFoundDevices;

    @FXML
    public void onButtonStartClick() {
        System.out.println("Start");
        for (String getFoundedDevice: getSelectedFoundedDevices()) {
            String[] split = getFoundedDevice.split("/");
            byte logicNumber = Byte.parseByte(split[0]);
            if(choiceCommand.getSelectionModel().selectedItemProperty().getValue().equals(MEASUREMENT_COMMAND))
                ExecutorBlockDetectionCommands.addCommand(
                        new BlockDetectionCommands.StartMeasure(parent.getBdController(), logicNumber));
        }

    }


    @FXML
    public void onButtonStopClick() {
        System.out.println("Stop");
        ExecutorBlockDetectionCommands.addCommand(new BlockDetectionCommands.StopMeasure(parent.getBdController(), (byte) 1));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceCommands();
    }

    private void initChoiceCommands() {
        choiceCommand.setItems(FXCollections.observableArrayList(
                MEASUREMENT_COMMAND,
                CONTINUING_MEASUREMENT_COMMAND,
                SOURCE_SEARCH_COMMAND,
                CONTINUING_SOURCE_SEARCH_COMMAND,
                CALIBRATION_COMMAND,
                SETTING_PARAMETERS_COMMAND,
                TEMPERATURE_MEASUREMENT_COMMAND,
                READING_SPECTRS_FROM_BOI_COMMAND,
                LOADING_DATA_IN_BOI_COMMAND,
                DEVICE_SEARCH_COMMAND));

        SingleSelectionModel selectionMode = choiceCommand.getSelectionModel();
        selectionMode.selectFirst();
        selectionMode.selectedItemProperty()
                .addListener( (observable, oldValue, newValue) -> {
                    System.out.println("oldValue = " + oldValue);
                    System.out.println("newValue = " + newValue);
                });
    }



    private int indexX=0;
    private int indexY=0;
    private int COLUMN = 5;
    public void addFoundedDevice(byte logicNumber, int serialNumber) {

        String title = String.valueOf(logicNumber)+"/"+String.valueOf(serialNumber);
        CheckBox checkBox = new CheckBox(title);
        checkBox.setPadding(new Insets(0,5,5,0));

        if(indexX == COLUMN) {
            indexX =0;
            indexY++;
        }
        boxFoundDevices.add(checkBox, indexX++, indexY);
    }

    public List<String> getSelectedFoundedDevices() {
        List list = new ArrayList();
        Iterator<Node> iterator = boxFoundDevices.getChildren().iterator();
        for (Iterator<Node> it = iterator; it.hasNext(); ) {
            CheckBox checkBox = (CheckBox) it.next();
            if(checkBox.isSelected())
                list.add(checkBox.getText());
        }
        return list;
    }

    @Override
    public void setParent(RootController parent) {
        this.parent = parent;
    }
}
