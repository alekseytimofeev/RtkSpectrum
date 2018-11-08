package widget;

import detectionModules.BlockDetectionController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import widget.controllers.*;

import java.io.IOException;

import static detectionModules.ExecutorBlockDetectionCommands.shutDown;
import static widget.controllers.RootController.*;

public class Widget extends Application {

    private static BlockDetectionController blockDetectionController;

    public static void initialize(String[] args, BlockDetectionController controller) {
        System.out.println("Widget initialize\t" + Thread.currentThread().getName());
        blockDetectionController = controller;
        launch(args);
        System.out.println("Widget deInitialize\t" + Thread.currentThread().getName());
    }

    @Override
    public void start(Stage primaryStage)  {
        System.out.println("Widget start\t" + Thread.currentThread().getName());
        PaneAndController paneAndController = getRootPane();
        Pane rootPane = paneAndController.pane;
        RootController rootController = (RootController) paneAndController.controller;
        rootPane.getChildren().forEach(node -> {
            String id = node.getId();
            if(id != null) {
                if(id.equals("tabs") && node instanceof TabPane) {
                    ObservableList<Tab> tabs = ((TabPane) node).getTabs();
                    //tabs.add(new Tab("Контроль",    getControlPane(rootController).pane));
                    //tabs.add(new Tab("Измерение",   getMeasurePane(rootController).pane));
                    tabs.add(new Tab("Наблюдение",  getObservationPane(rootController).pane));
//                    tabs.add(new Tab("Хранение",    getStoragePane(rootController).pane));
//                    tabs.add(new Tab("База",        getBasePane(rootController).pane));
//                    tabs.add(new Tab("Обработка",   getProcessingPane(rootController).pane));
//                    tabs.add(new Tab("Эталоны",     getIdealModelsPane(rootController).pane));
//                    tabs.add(new Tab("Параметры",   getParametersPane(rootController).pane));
                }
                else if (id.equals("header") && node instanceof StackPane) {
                    ((StackPane) node).getChildren().add(getHeaderPane(rootController).pane);
                }
                else if(id.equals("bottom")) {
                    ((StackPane) node).getChildren().add(getBottomPane(rootController).pane);
                }
            }
        });

        primaryStage.setTitle("Spectrum");
        primaryStage.setScene(new Scene(rootPane, 900, 550));
        primaryStage.show();
    }

    private PaneAndController getRootPane() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/fxml/Root.fxml"));
        Pane rootPane = null;
        try {
            rootPane = loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        RootController rootController = loader.getController();
        rootController.setBlockDetectionController(blockDetectionController);
        return new PaneAndController(rootPane, rootController);
    }
    private PaneAndController getHeaderPane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/Header.fxml"));
        Pane headerPane = null;
        try {
            headerPane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        HeaderController headerController = loader.getController();
        headerController.setParent(rootController);
        rootController.addChild(HEADER_PANE_NAME, headerController);
        return  new PaneAndController(headerPane, headerController);
    }
    private PaneAndController getBottomPane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/Bottom.fxml"));
        Pane bottomPane = null;
        try {
            bottomPane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        BottomController bottomController = loader.getController();
        bottomController.setParent(rootController);
        rootController.addChild(BOTTOM_PANE_NAME, bottomController);
        return new PaneAndController(bottomPane, bottomController);
    }
    private PaneAndController getControlPane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/Control.fxml"));
        Pane controlPane = null;
        try {
            controlPane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ControlController controlController = loader.getController();
        controlController.setParent(rootController);
        rootController.addChild(CONTROL_PANE_NAME, controlController);
        return new PaneAndController(controlPane,controlController);
    }
    private PaneAndController getMeasurePane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/Measurement.fxml"));
        Pane measurementPane = null;
        try {
            measurementPane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        MeasurementController measurementController = loader.getController();
        measurementController.setParent(rootController);
        rootController.addChild(MEASUREMENT_PANE_NAME, measurementController);
        return new PaneAndController(measurementPane, measurementController);
    }
    private PaneAndController getObservationPane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/Observation.fxml"));
        Pane observationPane = null;
        try {
            observationPane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ObservationController observationController = loader.getController();
        observationController.setParent(rootController);
        rootController.addChild(OBSERVATION_PANE_NAME, observationController);
        return new PaneAndController(observationPane,observationController);
    }
    private PaneAndController getStoragePane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/Storage.fxml"));
        Pane storagePane = null;
        try {
            storagePane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        StorageController storageController = loader.getController();
        storageController.setParent(rootController);
        rootController.addChild(STORAGE_PANE_NAME, storageController);
        return new PaneAndController(storagePane, storageController);
    }
    private PaneAndController getBasePane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/DataBase.fxml"));
        Pane dataBasePane = null;
        try {
            dataBasePane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        DataBaseController dataBaseController = loader.getController();
        dataBaseController.setParent(rootController);
        rootController.addChild(DATA_BASE_PANE_NAME, dataBaseController);
        return new PaneAndController(dataBasePane, dataBaseController);
    }
    private PaneAndController getProcessingPane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/Processing.fxml"));
        Pane processingPane = null;
        try {
            processingPane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ProcessingController processingController = loader.getController();
        processingController.setParent(rootController);
        rootController.addChild(PROCESSING_PANE_NAME, processingController);
        return new PaneAndController(processingPane, processingController);
    }
    private PaneAndController getIdealModelsPane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/IdealModels.fxml"));
        Pane idealModelsPane = null;
        try {
            idealModelsPane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        IdealModelsController idealModelsController = loader.getController();
        idealModelsController.setParent(rootController);
        rootController.addChild(IDEAL_MODELS_PANE_NAME, idealModelsController);
        return new PaneAndController(idealModelsPane, idealModelsController);
    }
    private PaneAndController getParametersPane(RootController rootController) {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../../resources/fxml/Parameters.fxml"));
        Pane parametersPane = null;
        try {
            parametersPane = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ParametersController parametersController = loader.getController();
        parametersController.setParent(rootController);
        rootController.addChild(PARAMETERS_PANE_NAME, parametersController);
        return new PaneAndController(parametersPane, parametersController);
    }

    private static class PaneAndController {
        Pane pane;
        Object controller;

        public PaneAndController(Pane pane, Object controller) {
            this.pane = pane;
            this.controller = controller;
        }
    }

    @Override
    public void stop() {
        shutDown();
    }
}
