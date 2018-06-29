package widget;


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import widget.controllers.*;

import java.io.IOException;import java.util.ResourceBundle;

public class Widget extends Application {

    private RootController          rootController;
    private HeaderController        headerController;
    private ControlController       controlController;
    private MeasurementController   measurementController;
    private ObservationController   observationController;
    private StorageController       storageController;
    private DataBaseController      dataBaseController;
    private ProcessingController    processingController;
    private IdealModelsController   idealModelsController;
    private ParametersController    parametersController;

    public static void initialize(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        Class<? extends Widget> aClass = getClass();
        FXMLLoader loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Root.fxml"));
        Pane rootPane = loader.load();
        ResourceBundle resources = null;
        loader.setResources(resources);
        rootController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Header.fxml"));
        Pane headerPane = loader.load();
        resources = null;
        loader.setResources(resources);
        headerController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Control.fxml"));
        Pane controlPane = loader.load();
        resources = null;
        loader.setResources(resources);
        controlController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Measurement.fxml"));
        Pane measurementPane = loader.load();
        resources = null;
        loader.setResources(resources);
        measurementController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Observation.fxml"));
        Pane observationPane = loader.load();
        resources = null;
        loader.setResources(resources);
        observationController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Storage.fxml"));
        Pane storagePane = loader.load();
        resources = null;
        loader.setResources(resources);
        storageController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/DataBase.fxml"));
        Pane dataBasePane = loader.load();
        resources = null;
        loader.setResources(resources);
        dataBaseController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Processing.fxml"));
        Pane processingPane = loader.load();
        resources = null;
        loader.setResources(resources);
        processingController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/IdealModels.fxml"));
        Pane idealModelsPane = loader.load();
        resources = null;
        loader.setResources(resources);
        idealModelsController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Parameters.fxml"));
        Pane parametersPane = loader.load();
        resources = null;
        loader.setResources(resources);
        parametersController = loader.getController();


        rootPane.getChildren().forEach(node -> {
            String id = node.getId();
            if (id != null)
            {
                if (id.equals("tabs") && node instanceof TabPane)
                {
                    ObservableList<Tab> tabs = ((TabPane) node).getTabs();
                    Tab controlTab = new Tab("Контроль", controlPane);
                    Tab measurementTab = new Tab("Измерение", measurementPane);
                    Tab observationTab = new Tab("Наблюдение", observationPane);
                    Tab storageTab = new Tab("Хранение", storagePane);
                    Tab dataBaseTab = new Tab("База", dataBasePane);
                    Tab processingTab = new Tab("Обработка", processingPane);
                    Tab idealModelsTab = new Tab("Эталоны", idealModelsPane);
                    Tab parametersTab = new Tab("Параметры", parametersPane);

                    tabs.add(controlTab);
                    tabs.add(measurementTab);
                    tabs.add(observationTab);
                    tabs.add(storageTab);
                    tabs.add(dataBaseTab);
                    tabs.add(processingTab);
                    tabs.add(idealModelsTab);
                    tabs.add(parametersTab);
                }
                else if(id.equals("header")  && node instanceof StackPane) {
                    ((StackPane) node).getChildren().add(headerPane);
                }
            }
        });

        primaryStage.setTitle("Spectrum");
        primaryStage.setScene(new Scene(rootPane, 900, 550));

        primaryStage.show();

    }

}
