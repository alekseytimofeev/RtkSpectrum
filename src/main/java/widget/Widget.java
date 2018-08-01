package widget;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import widget.controllers.*;

import java.io.IOException;

public class Widget extends Application {

    public static RootController    rootController;

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
        System.out.println("Widget initialize\t" + Thread.currentThread().getName());
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        System.out.println("Widget start\t" + Thread.currentThread().getName());

        Class<? extends Widget> aClass = getClass();
        FXMLLoader loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Root.fxml"));
        Pane rootPane = loader.load();
        rootController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Header.fxml"));
        Pane headerPane = loader.load();
        headerController = loader.getController();

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Control.fxml"));
        Pane controlPane = loader.load();
        controlController = loader.getController();
        controlController.setParent(rootController);
        rootController.addChild(controlController);

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Measurement.fxml"));
        Pane measurementPane = loader.load();
        measurementController = loader.getController();
        measurementController.setParent(rootController);
        rootController.addChild(measurementController);

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Observation.fxml"));
        Pane observationPane = loader.load();
        observationController = loader.getController();
        observationController.setParent(rootController);
        rootController.addChild(observationController);

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Storage.fxml"));
        Pane storagePane = loader.load();
        storageController = loader.getController();
        storageController.setParent(rootController);
        rootController.addChild(storageController);

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/DataBase.fxml"));
        Pane dataBasePane = loader.load();
        dataBaseController = loader.getController();
        dataBaseController.setParent(rootController);
        rootController.addChild(dataBaseController);

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Processing.fxml"));
        Pane processingPane = loader.load();
        processingController = loader.getController();
        processingController.setParent(rootController);
        rootController.addChild(processingController);

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/IdealModels.fxml"));
        Pane idealModelsPane = loader.load();
        idealModelsController = loader.getController();
        idealModelsController.setParent(rootController);
        rootController.addChild(idealModelsController);

        loader = new FXMLLoader(aClass.getResource("../../resources/fxml/Parameters.fxml"));
        Pane parametersPane = loader.load();
        parametersController = loader.getController();
        parametersController.setParent(rootController);
        rootController.addChild(parametersController);

        rootPane.getChildren().forEach(node -> {
            String id = node.getId();
            if (id != null) {
                if (id.equals("tabs") && node instanceof TabPane) {
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
                else if (id.equals("header") && node instanceof StackPane) {
                    ((StackPane) node).getChildren().add(headerPane);
                }
            }
        });

        primaryStage.setTitle("Spectrum");
        primaryStage.setScene(new Scene(rootPane, 900, 550));

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Widget stop\t" + Thread.currentThread().getName());
    }

    public interface Parentable {
        void setParent(RootController parent);
    }
}
